# Reporte de Handoff (Milestone 1: Optimización de corp-spring-boot-starter)

**Autor**: Explorer M1  
**Fecha**: 2026-07-29  
**Estado**: Completado (Informe de Análisis y Diseño Técnico Listo para Implementación)  
**Repositorio de Análisis**: `/home/jaruiz/Desarrollo/corp-spring-boot-starter`  

---

## 1. Observation (Observación Directa del Estado Actual)

Tras analizar quirúrgicamente el repositorio `/home/jaruiz/Desarrollo/corp-spring-boot-starter`, se observan los siguientes hechos y estructuras de código:

### 1.1 Configuración de Proyecto y Dependencias Maven (`pom.xml`)
- **Maven Coordinates**: `com.corp.tenant:corp-spring-boot-starter:1.0.0`
- **Parent**: `org.springframework.boot:spring-boot-starter-parent:4.1.0`
- **Java**: Versión `25` con `<maven.compiler.enablePreview>true</maven.compiler.enablePreview>`.
- **Dependencias Actuales**:
  - `spring-boot-starter-web` (scope: `provided`)
  - `spring-boot-autoconfigure`
  - `jakarta.servlet-api` (scope: `provided`)
  - `slf4j-api`
  - `spring-boot-starter-test` (scope: `test`)
  - Plugin `pitest-maven` (versión `1.15.3`, umbral de mutación 90%).
- **Ausencias**: No existen dependencias gRPC (`io.grpc:grpc-api`, `io.grpc:grpc-stub`, etc.) declaradas en `pom.xml`.

### 1.2 Componentes de Autoconfiguración y Contexto Existentes
- **`com.corp.tenant.TenantContext`** (`src/main/java/com/corp/tenant/TenantContext.java`):
  - Gestiona el identificador de tenant usando `ScopedValue<String>` (primario) de Java 25 y `ThreadLocal<String>` (fallback).
  - Incluye `runWithTenant(String, Runnable)`, `resolveTenant()`, y `getZoneIdForTenant(String)`.
- **`com.corp.tenant.TenantFilter`** (`src/main/java/com/corp/tenant/TenantFilter.java`):
  - Filtro HTTP Servlet (`OncePerRequestFilter`) que extrae `X-Tenant-ID` o parámetro `tenant`, enlazándolo al `TenantContext`. Omite `/actuator` y `/static/`.
- **`com.corp.tenant.TenantAutoConfiguration`** (`src/main/java/com/corp/tenant/TenantAutoConfiguration.java`):
  - Posee anotaciones `@AutoConfiguration`, `@ConditionalOnWebApplication(type = SERVLET)`, `@ConditionalOnProperty(prefix = "corp.tenant", name = "enabled", havingValue = "true", matchIfMissing = true)`.
  - Configura el bean `TenantFilter` anotado con `@ConditionalOnMissingBean` y `@Order(1)`.
- **`com.corp.telemetry.W3cTraceContextFilter`** (`src/main/java/com/corp/telemetry/W3cTraceContextFilter.java`):
  - Filtro HTTP Servlet (`OncePerRequestFilter`) que procesa la cabecera W3C `traceparent` (`00-{trace_id}-{parent_id}-{flags}`) o genera trace ID fallback de 32 caracteres hexadecimales, inyectando `trace_id` en el MDC de SLF4J y devolviendo `traceparent` en la respuesta HTTP.
- **`com.corp.telemetry.TelemetryAutoConfiguration`** (`src/main/java/com/corp/telemetry/TelemetryAutoConfiguration.java`):
  - Posee anotaciones `@AutoConfiguration`, `@ConditionalOnWebApplication(type = SERVLET)`, `@ConditionalOnProperty(prefix = "corp.telemetry", name = "enabled", havingValue = "true", matchIfMissing = true)`.
  - Configura el bean `W3cTraceContextFilter` anotado con `@ConditionalOnMissingBean` y `@Order(1)`.
- **Importaciones de Autoconfiguración**:
  - `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` contiene:
    ```
    com.corp.tenant.TenantAutoConfiguration
    com.corp.telemetry.TelemetryAutoConfiguration
    ```

### 1.3 Carencias Identificadas respecto a los Requisitos del Hito 1
1. **Ausencia de Interceptores gRPC**: No existen interceptores gRPC tanto para cliente (`ClientInterceptor`) como servidor (`ServerInterceptor`) para propagar W3C `traceparent` y `X-Tenant-ID`.
2. **Restricción Exclusiva a Servlet**: Tanto `TelemetryAutoConfiguration` como `TenantAutoConfiguration` tienen la condición `@ConditionalOnWebApplication(type = SERVLET)`. Esto impide que microservicios puros gRPC (sin Tomcat/Servlet container) o servicios reactivos carguen la trazabilidad corporativa o el contexto de tenant.
3. **Ausencia de Infraestructura Leyden CDS y AOT**:
   - No existe la clase `LeydenAotRuntimeHints` ni registro en `META-INF/spring/aot.factories`.
   - No existe script de warmup/entrenamiento (`-Dspring.context.exit=on-refresh`) ni generación de archivo `.jsa` (Class Data Sharing).

---

## 2. Logic Chain (Cadena Lógica de Razonamiento)

1. **Sobre la Extensibilidad de Autoconfiguraciones (`@ConditionalOnMissingBean`)**:
   - *Premisa*: El starter debe ser 100% extensible en microservicios cliente (`SaaSRegantes`, `pctMultiMicroservices`, `AppViajes`) sin forzar dependencias Web Servlet cuando se utilicen microservicios gRPC o workers en segundo plano.
   - *Razonamiento*: Se debe desacoplar la autoconfiguración base/gRPC de la autoconfiguración Servlet. Cada bean configurado dinámicamente (`W3cTraceContextFilter`, `W3cGrpcServerInterceptor`, `W3cGrpcClientInterceptor`, `TenantFilter`) debe llevar `@ConditionalOnMissingBean` con el tipo explícito de la clase para permitir que el microservicio consumidor sobreescriba cualquier componente mediante la declaración de su propio `@Bean`.

2. **Sobre los Interceptores gRPC / W3C `traceparent` (Cliente y Servidor)**:
   - *Premisa*: gRPC utiliza cabeceras binarias o ASCII en la estructura `io.grpc.Metadata`. La clave normalizada W3C es `traceparent` (ASCII String).
   - *En el Servidor (`W3cGrpcServerInterceptor`)*:
     - El servidor gRPC recibe la solicitud en `interceptCall(ServerCall, Metadata, ServerCallHandler)`.
     - *Desafío Técnico*: gRPC invoca los callbacks de la solicitud (`onMessage`, `onHalfClose`, `onComplete`, etc.) de forma asíncrona en hilos del pool executor o en hilos virtuales Loom. Colocar el `trace_id` en el MDC solo dentro del método `interceptCall` no propagará el contexto al código del servicio ejecutado en los callbacks.
     - *Solución Arquitectónica*: `W3cGrpcServerInterceptor` debe retornar un `ForwardingServerCallListener.SimpleForwardingServerCallListener` que envuelva cada método de callback (`onMessage`, `onHalfClose`, `onCancel`, `onComplete`, `onReady`) dentro de un bloque try-finally que inyecte `MDC.put("trace_id", traceId)` y `TenantContext.runWithTenant(...)`, limpiándolo inmediatamente al terminar la llamada.
   - *En el Cliente (`W3cGrpcClientInterceptor`)*:
     - El cliente gRPC realiza una llamada saliente mediante `interceptCall(MethodDescriptor, CallOptions, Channel)`.
     - *Solución*: Inyecta la cabecera `traceparent` en los `headers` de la llamada saliente usando el `trace_id` actual del MDC (o generando un UUID fallback de 32 hex + span ID de 16 hex: `00-{trace_id}-{span_id}-01`). Asimismo, propaga `x-tenant-id` si existe en `TenantContext.getTenantId()`.

3. **Sobre el Entorno Leyden CDS y Compatibilidad AOT / GraalVM Native Image**:
   - *Premisa*: Project Leyden y CDS requieren que la JVM registre las clases durante una fase de entrenamiento (`ArchiveClassesAtExit`). Spring Boot 4 provee `-Dspring.context.exit=on-refresh` para inicializar el contenedor, registrar reflexiones/hints AOT y cerrar de forma limpia (`exit code 0`).
   - *Razonamiento*:
     1. Se creará la clase `com.corp.aot.LeydenAotRuntimeHints` implementando `RuntimeHintsRegistrar`.
     2. Se registrarán pistas de reflexión para `TenantContext`, `TenantFilter`, `W3cTraceContextFilter`, `W3cGrpcServerInterceptor`, `W3cGrpcClientInterceptor` y `ScopedValue`.
     3. Se añadirá `META-INF/spring/aot.factories`.
     4. Se proveerá el script ejecutable `scripts/leyden-warmup.sh` que realiza la compilación, ejecuta el entrenamiento con `-Dspring.context.exit=on-refresh -XX:ArchiveClassesAtExit=target/application.jsa` y valida la ejecución posterior con `-XX:SharedArchiveFile=target/application.jsa` para garantizar arranque en Cloud Run <100ms.

---

## 3. Caveats (Advertencias y Limitaciones)

- **Modo Solo Lectura**: Como agente de investigación, no se ha modificado directamente el código fuente en `/home/jaruiz/Desarrollo/corp-spring-boot-starter`. Las clases y scripts detallados a continuación constituyen el diseño exacto listo para implementación por los agentes programadores.
- **Dependencias gRPC como `provided`**: Para evitar engordar aplicaciones que solo usan HTTP/REST, las dependencias gRPC en `pom.xml` del starter deben tener scope `<scope>provided</scope>`. La autoconfiguración de gRPC usará `@ConditionalOnClass({io.grpc.ServerInterceptor.class, io.grpc.ClientInterceptor.class})`.
- **Soporte de Preview Features en JDK 25**: Se requiere la bandera `--enable-preview` en la JVM para la API `ScopedValue` de Java 25.

---

## 4. Conclusion (Diseño Detallado de Implementación)

### 4.1 Lista de Archivos a Modificar y Crear

| Archivo | Acción | Propósito |
|---|---|---|
| `pom.xml` | Modificar | Agregar dependencias gRPC (`scope: provided`) y plugin AOT. |
| `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` | Modificar | Registrar la nueva autoconfiguración `GrpcTelemetryAutoConfiguration`. |
| `src/main/java/com/corp/telemetry/TelemetryAutoConfiguration.java` | Modificar | Estructurar autoconfiguración extensible con `@ConditionalOnMissingBean` y modularización Web. |
| `src/main/java/com/corp/tenant/TenantAutoConfiguration.java` | Modificar | Estructurar autoconfiguración extensible para Tenant. |
| `src/main/java/com/corp/telemetry/grpc/GrpcTraceContext.java` | **Crear** | Constantes y llaves `Metadata.Key` para `traceparent` y `x-tenant-id`. |
| `src/main/java/com/corp/telemetry/grpc/W3cGrpcServerInterceptor.java` | **Crear** | Interceptor de servidor gRPC con envoltorio de listeners para MDC y ScopedValue. |
| `src/main/java/com/corp/telemetry/grpc/W3cGrpcClientInterceptor.java` | **Crear** | Interceptor de cliente gRPC para inyección de `traceparent` y `x-tenant-id`. |
| `src/main/java/com/corp/telemetry/grpc/GrpcTelemetryAutoConfiguration.java` | **Crear** | Autoconfiguración Spring Boot para interceptores gRPC. |
| `src/main/java/com/corp/aot/LeydenAotRuntimeHints.java` | **Crear** | Registro de runtime hints AOT para GraalVM y Leyden CDS. |
| `src/main/resources/META-INF/spring/aot.factories` | **Crear** | Declaración del registrador de hints AOT. |
| `scripts/leyden-warmup.sh` | **Crear** | Script bash de entrenamiento CDS (`on-refresh`) y generación de `.jsa`. |
| `src/test/java/com/corp/telemetry/grpc/W3cGrpcServerInterceptorTest.java` | **Crear** | Pruebas unitarias del interceptor gRPC de servidor. |
| `src/test/java/com/corp/telemetry/grpc/W3cGrpcClientInterceptorTest.java` | **Crear** | Pruebas unitarias del interceptor gRPC de cliente. |
| `src/test/java/com/corp/telemetry/grpc/GrpcTelemetryAutoConfigurationTest.java` | **Crear** | Pruebas de autoconfiguración y sobreescritura de beans. |
| `src/test/java/com/corp/aot/LeydenAotRuntimeHintsTest.java` | **Crear** | Pruebas de registro de hints AOT. |

---

### 4.2 Código y Especificación Exacta de las Nuevas Clases

#### 1. `GrpcTraceContext.java` (`com.corp.telemetry.grpc`)
```java
package com.corp.telemetry.grpc;

import io.grpc.Metadata;

/**
 * Constantes y llaves de Metadata para trazabilidad W3C y multi-tenancy en gRPC.
 */
public final class GrpcTraceContext {
    public static final String TRACEPARENT_HEADER = "traceparent";
    public static final String X_TENANT_ID_HEADER = "x-tenant-id";
    public static final String MDC_TRACE_ID_KEY = "trace_id";

    public static final Metadata.Key<String> TRACEPARENT_METADATA_KEY =
            Metadata.Key.of(TRACEPARENT_HEADER, Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> TENANT_ID_METADATA_KEY =
            Metadata.Key.of(X_TENANT_ID_HEADER, Metadata.ASCII_STRING_MARSHALLER);

    private GrpcTraceContext() {}
}
```

#### 2. `W3cGrpcServerInterceptor.java` (`com.corp.telemetry.grpc`)
```java
package com.corp.telemetry.grpc;

import com.corp.tenant.TenantContext;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.MDC;
import java.util.UUID;

/**
 * Interceptor gRPC de Servidor para propagación del contexto W3C traceparent y X-Tenant-ID.
 * Garantiza que MDC y TenantContext permanezcan enlazados durante la ejecución de callbacks del Listener.
 */
public class W3cGrpcServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String traceparent = headers.get(GrpcTraceContext.TRACEPARENT_METADATA_KEY);
        String traceId;

        if (traceparent != null && traceparent.startsWith("00-")) {
            String[] parts = traceparent.split("-");
            if (parts.length >= 2 && !parts[1].isBlank()) {
                traceId = parts[1];
            } else {
                traceId = generateFallbackTraceId();
            }
        } else {
            traceId = generateFallbackTraceId();
        }

        String tenantId = headers.get(GrpcTraceContext.TENANT_ID_METADATA_KEY);

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                runWithContext(traceId, tenantId, () -> super.onMessage(message));
            }

            @Override
            public void onHalfClose() {
                runWithContext(traceId, tenantId, () -> super.onHalfClose());
            }

            @Override
            public void onCancel() {
                runWithContext(traceId, tenantId, () -> super.onCancel());
            }

            @Override
            public void onComplete() {
                runWithContext(traceId, tenantId, () -> super.onComplete());
            }

            @Override
            public void onReady() {
                runWithContext(traceId, tenantId, () -> super.onReady());
            }
        };
    }

    private void runWithContext(String traceId, String tenantId, Runnable runnable) {
        MDC.put(GrpcTraceContext.MDC_TRACE_ID_KEY, traceId);
        try {
            if (tenantId != null && !tenantId.isBlank()) {
                TenantContext.runWithTenant(tenantId, runnable);
            } else {
                runnable.run();
            }
        } finally {
            MDC.remove(GrpcTraceContext.MDC_TRACE_ID_KEY);
        }
    }

    private String generateFallbackTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
```

#### 3. `W3cGrpcClientInterceptor.java` (`com.corp.telemetry.grpc`)
```java
package com.corp.telemetry.grpc;

import com.corp.tenant.TenantContext;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import org.slf4j.MDC;
import java.util.UUID;

/**
 * Interceptor gRPC de Cliente para inyección del contexto W3C traceparent y X-Tenant-ID en llamadas salientes.
 */
public class W3cGrpcClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = MDC.get(GrpcTraceContext.MDC_TRACE_ID_KEY);
                if (traceId == null || traceId.isBlank()) {
                    traceId = UUID.randomUUID().toString().replace("-", "");
                }

                String spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
                String traceparent = "00-" + traceId + "-" + spanId + "-01";

                headers.put(GrpcTraceContext.TRACEPARENT_METADATA_KEY, traceparent);

                String tenantId = TenantContext.getTenantId();
                if (tenantId != null && !tenantId.isBlank()) {
                    headers.put(GrpcTraceContext.TENANT_ID_METADATA_KEY, tenantId);
                }

                super.start(responseListener, headers);
            }
        };
    }
}
```

#### 4. `GrpcTelemetryAutoConfiguration.java` (`com.corp.telemetry.grpc`)
```java
package com.corp.telemetry.grpc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * Autoconfiguración Spring Boot para la trazabilidad gRPC W3C OpenTelemetry.
 */
@AutoConfiguration
@ConditionalOnClass({io.grpc.ServerInterceptor.class, io.grpc.ClientInterceptor.class})
@ConditionalOnProperty(prefix = "corp.telemetry.grpc", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GrpcTelemetryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(W3cGrpcServerInterceptor.class)
    @Order(10)
    public W3cGrpcServerInterceptor w3cGrpcServerInterceptor() {
        return new W3cGrpcServerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(W3cGrpcClientInterceptor.class)
    @Order(10)
    public W3cGrpcClientInterceptor w3cGrpcClientInterceptor() {
        return new W3cGrpcClientInterceptor();
    }
}
```

#### 5. Modificaciones en `TelemetryAutoConfiguration.java` y `TenantAutoConfiguration.java`
Ambas clases de autoconfiguración deben modularizar las beans Web Servlet en clases anidadas estáticas anotadas con `@ConditionalOnWebApplication(type = SERVLET)` y asegurar que cada bean lleve la anotación `@ConditionalOnMissingBean(ClassTarget.class)` para permitir que cualquier aplicación sustituya las beans dinámicamente.

#### 6. `LeydenAotRuntimeHints.java` (`com.corp.aot`)
```java
package com.corp.aot;

import com.corp.telemetry.W3cTraceContextFilter;
import com.corp.telemetry.grpc.W3cGrpcClientInterceptor;
import com.corp.telemetry.grpc.W3cGrpcServerInterceptor;
import com.corp.tenant.TenantContext;
import com.corp.tenant.TenantFilter;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registrador de pistas en tiempo de ejecución (RuntimeHints) para AOT y Leyden Class Data Sharing (CDS).
 */
public class LeydenAotRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(TenantContext.class, MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.DECLARED_FIELDS);
        hints.reflection().registerType(TenantFilter.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(W3cTraceContextFilter.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(W3cGrpcServerInterceptor.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(W3cGrpcClientInterceptor.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);
    }
}
```

#### 7. Modificaciones a `pom.xml`
```xml
<!-- Añadir dependencias gRPC con scope provided -->
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-api</artifactId>
    <version>1.68.0</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-stub</artifactId>
    <version>1.68.0</version>
    <scope>provided</scope>
</dependency>
```

#### 8. Script de Warmup Leyden CDS (`scripts/leyden-warmup.sh`)
```bash
#!/usr/bin/env bash
set -euo pipefail

echo "=== Iniciando Warmup y Entrenamiento Leyden CDS para corp-spring-boot-starter ==="

APP_JAR="${1:-target/corp-spring-boot-starter-1.0.0.jar}"
JSA_ARCHIVE="target/application.jsa"

if [ ! -f "$APP_JAR" ]; then
    echo "ERROR: Archivo JAR no encontrado en $APP_JAR. Ejecute 'mvn package -DskipTests' primero."
    exit 1
fi

echo "[1/3] Paso 1: Ejecutando entrenamiento del contexto Spring Boot (-Dspring.context.exit=on-refresh)..."
java --enable-preview \
     -XX:ArchiveClassesAtExit="${JSA_ARCHIVE}" \
     -Dspring.context.exit=on-refresh \
     -Dspring.aot.enabled=true \
     -jar "${APP_JAR}" || true

if [ -f "${JSA_ARCHIVE}" ]; then
    echo "[2/3] Paso 2: ¡Archivo CDS (.jsa) generado exitosamente!"
    ls -lh "${JSA_ARCHIVE}"
else
    echo "ERROR: Falló la generación del archivo de archivo CDS ${JSA_ARCHIVE}"
    exit 1
fi

echo "[3/3] Paso 3: Verificando ejecución con Shared Archive File..."
java --enable-preview \
     -XX:SharedArchiveFile="${JSA_ARCHIVE}" \
     -Dspring.aot.enabled=true \
     -Dspring.context.exit=on-refresh \
     -jar "${APP_JAR}"

echo "=== Warmup CDS Leyden Completado Exitosamente. Inicio optimizado en Cloud Run (<100ms) ==="
```

---

## 5. Verification Method (Método de Verificación Recomendado para Workers)

Para auditar e independientemente verificar los cambios aplicados por los desarrolladores:

1. **Compilación y Pruebas Unitarias**:
   ```bash
   mvn clean test
   ```
   *Criterio de Aceptación*: Todos los tests unitarios (`W3cTraceContextFilterTest`, `W3cGrpcServerInterceptorTest`, `W3cGrpcClientInterceptorTest`, `TenantContextTest`, `TenantFilterTest`) deben pasar al 100%.

2. **Cobertura de Mutación con Pitest**:
   ```bash
   mvn pitest:mutationCoverage
   ```
   *Criterio de Aceptación*: La cobertura de mutaciones de PITest debe ser $\ge 90\%$.

3. **Verificación de Autoconfiguración y Sobreescritura de Beans**:
   - Usar `ApplicationContextRunner` en `GrpcTelemetryAutoConfigurationTest` para verificar que al declarar un bean personalizado `W3cGrpcServerInterceptor` en una configuración cliente, la autoconfiguración respeta `@ConditionalOnMissingBean` y no sobrescribe el bean del usuario.

4. **Verificación AOT Processing**:
   ```bash
   mvn spring-boot:process-aot
   ```
   *Criterio de Aceptación*: Generación limpia de clases y runtime hints sin errores de reflexión.

5. **Verificación del Script Warmup Leyden CDS**:
   ```bash
   chmod +x scripts/leyden-warmup.sh
   ./scripts/leyden-warmup.sh
   ```
   *Criterio de Aceptación*: El proceso genera `target/application.jsa` y finaliza con código de salida `0` durante el refresco contextual `-Dspring.context.exit=on-refresh`.
