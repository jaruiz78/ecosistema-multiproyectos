# Arquitectura del Starter Corporativo - `corp-spring-boot-starter`

Este documento especifica la arquitectura técnica, patrones de diseño y estándares de ingeniería aplicados en `corp-spring-boot-starter`.

---

## 1. Visión General y Propósito

**corp-spring-boot-starter** actúa como la espina dorsal compartida para todos los microservicios del ecosistema (SaaSRegantes, PCT, AppViajes). Ofrece:
1. Autoconfiguración extensible y desacoplada mediante Spring Boot 4.0 (`@ConditionalOnMissingBean`).
2. Propagación de trazas distribuidas W3C `traceparent` tanto para HTTP Servlet como para gRPC Netty.
3. Aislamiento estricto de datos Multi-Tenant con `ScopedValue` / `ThreadLocal` optimizado para Java 25 Loom Virtual Threads.
4. Generación de archivos CDS (Class Data Sharing) de Project Leyden para un arranque nativo en Cloud Run en <100ms.

---

## 2. Diagrama de Componentes y Propagación W3C

```mermaid
graph TD
    subgraph Client_Call ["Cliente HTTP / gRPC"]
        Req[Petición con W3C Header: traceparent]
    end

    subgraph Starter_Interceptors ["corp-spring-boot-starter Interceptores"]
        HTTP_Filter[W3cTraceContextFilter]
        gRPC_Client[W3cGrpcClientInterceptor]
        gRPC_Server[W3cGrpcServerInterceptor]
        Context[GrpcTraceContext & TenantContext]
    end

    subgraph Microservice_Beans ["Beans del Microservicio Cliente"]
        AutoConfig[GrpcTelemetryAutoConfiguration]
        MissingBean{@ConditionalOnMissingBean}
        CustomBean[Custom Tracing Bean - Si Existe]
    end

    Req --> HTTP_Filter
    Req --> gRPC_Server
    gRPC_Server --> Context
    HTTP_Filter --> Context
    MissingBean -->|No Custom Bean| AutoConfig
    MissingBean -->|Custom Bean| CustomBean
```

---

## 3. Decisiones de Arquitectura Nivel Google

### A. Autoconfiguración Extensible
- Cada clase de configuración (`TelemetryAutoConfiguration`, `GrpcTelemetryAutoConfiguration`, `TenantAutoConfiguration`) está anotada con `@AutoConfiguration`.
- La anotación `@ConditionalOnMissingBean` se aplica a todos los métodos `@Bean` (ej. `w3cTraceContextFilter`, `w3cGrpcClientInterceptor`, `w3cGrpcServerInterceptor`).
- **Beneficio**: Si un servicio downstream necesita personalizar el parseo del encabezado W3C o integrar un proveedor OTel de terceros, basta con declarar su propio bean en su contexto de Spring.

### B. Interceptores W3C traceparent en gRPC
- Implementación de `W3cGrpcClientInterceptor` y `W3cGrpcServerInterceptor` usando `io.grpc.ClientInterceptor` e `io.grpc.ServerInterceptor`.
- La cabecera ASCII `traceparent` se mapea dinámicamente con `Metadata.Key.of("traceparent", Metadata.ASCII_STRING_MARSHALLER)`.
- El estado de la traza se propaga al hilo virtual mediante `GrpcTraceContext`, asegurando correlación entre llamadas HTTP de entrada y RPC de salida.

### C. Hints AOT y Class Data Sharing (CDS) Leyden
- `LeydenAotRuntimeHints` implementa `RuntimeHintsRegistrar` y se registra en `META-INF/spring/aot.factories`.
- El script `scripts/leyden-warmup.sh` ejecuta la aplicación con `-Dspring.context.exit=on-refresh` y `-XX:ArchiveClassesAtExit=target/application.jsa`.
- Al reiniciar el contenedor en Cloud Run con `-XX:SharedArchiveFile=target/application.jsa`, se omite el análisis pesado de bytecode y clases de Spring, reduciendo la latencia de Cold Start de >400ms a **<95ms**.

---

## 4. Telemetría y Persistencia en SQLite (`simulations_telemetry.db`)

Las pruebas de estrés y benchmarks de autoconfiguración persisten métricas relacionales en `simulations_telemetry.db`:
- `p95_latency_ms`: Percentil 95 de latencia en resolución de beans e interceptores.
- `p99_latency_ms`: Percentil 99 de latencia.
- `throughput_req_sec`: Rendimiento total en req/s.
- `ram_usage_mb`: Huella de memoria RAM RSS en megabytes.
