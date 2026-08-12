# Informe de Handoff — Explorador de Remediación (Explorer M3 Gen 2)

**Proyecto**: Optimización de `pctMultiMicroservices` (`services/backend-java`)  
**Fecha**: 2026-07-29  
**Agente**: Explorer M3 Gen 2  
**Estado**: Análisis Completado & Plan de Remediación Validado (274/274 PASS)

---

## 1. Observation

### 1.1 Evidencia del Revisor (`reviewer_m3`)
El Revisor vetó el Hito 3 en la Iteración 1 reportando un `BUILD FAILURE` con **178 Errores y 6 Fallos** al ejecutar `./mvnw test` en `services/backend-java`. Los mensajes clave extraídos del informe del revisor son:
- **MapStruct Failure**: `ClassNotFoundException: Cannot find implementation for com.pct.integracion.infrastructure.adapter.out.taxicaller.mapper.TaxiCallerMapper`.
- **Bytecode / ByteBuddy Exception**: `IllegalArgumentException: Could not create type` y `NoClassDefFoundError` en la instanciación de Mocks de Mockito bajo Java 25 preview features.
- **Spring ApplicationContext Cascade Failure**: `IllegalState ApplicationContext failure threshold (1) exceeded`, provocado por la incapacidad de inyectar beans dependientes de mappers de MapStruct no compilados.

### 1.2 Hallazgos de Investigación Directa en la Base de Código

#### A. Configuración de `pom.xml` (`services/backend-java/pom.xml`)
- **MapStruct Processor (Líneas 446-452)**:
  `maven-compiler-plugin` (v3.13.0) define `annotationProcessorPaths` con `org.mapstruct:mapstruct-processor:1.6.3` y argument `<arg>-Amapstruct.defaultComponentModel=spring</arg>`.
- **Surefire / Failsafe JVM Arguments (Líneas 509-511 y 526)**:
  `maven-surefire-plugin` (v3.5.2) y `maven-failsafe-plugin` (v3.5.2) configuran:
  `<argLine>@{argLine} --enable-preview -Dnet.bytebuddy.experimental=true --enable-native-access=ALL-UNNAMED</argLine>`.
- **Protobuf Maven Plugin (Líneas 389-408)**:
  `protobuf-maven-plugin` (v0.6.1) extrae protoc-dependencies a `${project.basedir}/target/protoc-dependencies/`.

#### B. Generación de Artefactos de MapStruct
Al ejecutar `./mvnw test-compile` o `./mvnw compile`, `maven-compiler-plugin` ejecuta correctamente el procesador de anotaciones de MapStruct y genera las siguientes 7 implementaciones en `target/generated-sources/annotations`:
1. `TaxiCallerMapperImpl.java`
2. `TaxiCallerWebhookMapperImpl.java`
3. `HbxMapperImpl.java`
4. `HbxBookingMapperImpl.java`
5. `BookingMappingMapperImpl.java`
6. `SyncStateMapperImpl.java`
7. `JobSyncStatusMapperImpl.java`

#### C. Conflicto de Limpieza de Archivos Temporales en `protobuf-maven-plugin`
En entornos donde el directorio `target/protoc-dependencies` retiene permisos de sólo lectura de una ejecución previa, el plugin `protobuf-maven-plugin:compile` falla en runtime al no poder borrar archivos temporales (`Unable to clean up temporary proto file directory`). Si esta fase falla o se interrumpe, la compilación de Java y el procesador de MapStruct no se ejecutan, dejando las implementaciones mappers ausentes durante la fase de ejecución de pruebas.

#### D. Verificación Real de la Suite de Pruebas Java
Al corregir permisos sobre `target` y ejecutar `./mvnw clean test`:
```text
[INFO] Results:
[INFO] Tests run: 274, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
**Total de pruebas ejecutadas**: 274  
**Fallos / Errores**: 0 (100% de éxito en unitarias e integración).

#### E. Suite de Pruebas de Go BFF (`services/bff-go`)
```text
go test -v ./...
PASS: 10/10 tests pasados en 0.007s.
```

---

## 2. Logic Chain

```
[Observación 1.1 & 1.2.C] protobuf-maven-plugin interrumpe la fase compile por fallo de permisos/cleanup en target/protoc-dependencies
       │
       ▼
[Observación 1.2.B] maven-compiler-plugin NO ejecuta mapstruct-processor; no se generan TaxiCallerMapperImpl y otros mappers
       │
       ▼
[Observación 1.1 & 1.2.A] Surefire intenta ejecutar los 274 tests -> Mappers.getMapper(TaxiCallerMapper.class) arroja ClassNotFoundException
       │
       ▼
[Observación 1.1] Spring Boot Context no encuentra los beans mappers en los @SpringBootTest -> Falla la inyección de dependencias
       │
       ▼
[Observación 1.1] Spring TestContext marca ApplicationContext failure threshold (1) exceeded -> Se desploman en cascada 178 tests
       │
       ▼
[Remediación Validada] Al ejecutar un ciclo limpio (clean test-compile / clean test) con permisos de escritura adecuados:
 1. protobuf-maven-plugin compila las clases gRPC sin errores.
 2. mapstruct-processor genera los 7 mappers en target/generated-sources/annotations.
 3. Surefire recibe -Dnet.bytebuddy.experimental=true permitiendo la ejecución de ByteBuddy/Mockito en Java 25.
 4. Todos los 274 tests pasan a BUILD SUCCESS (0 fallos, 0 errores).
```

---

## 3. Caveats

1. **Permisos del Sistema de Archivos**: En Linux, el plugin `protobuf-maven-plugin` genera archivos en `target/protoc-dependencies` que en ciertas circunstancias pierden permiso de modificación. Si se ejecuta `mvn clean` o `mvn test` como usuario sin elevación sobre un `target` creado previamente por otro usuario/contenedor, Maven fallará en la fase `clean` o `generate-sources`. Se debe asegurar que `target` tenga permisos de usuario `u+w`.
2. **Política Zero-Mockito**: Aunque Mockito está respaldado por `-Dnet.bytebuddy.experimental=true` en Surefire, el estándar del proyecto en la capa de dominio (`domain/`) exige Java puro sin mocks (usando Stubs in-memory).

---

## 4. Conclusion

Los 178 errores reportados en la Iteración 1 **no eran causados por defectos en el código de negocio**, sino por un fallo en la fase de generación de mappers de MapStruct provocado por la interrupción de la compilación de proto/annotations y/o ejecución directa de Surefire sobre un `target` desincronizado.

### Plan de Remediación Detallado para Worker (Iteración 2):

1. **Garantizar la Generación de Mappers en MapStruct**:
   - En `services/backend-java/pom.xml`, verificar que `maven-compiler-plugin` mantenga configurado el procesador de anotaciones `org.mapstruct:mapstruct-processor:${mapstruct.version}` con la opción `-Amapstruct.defaultComponentModel=spring`.
   - Asegurar que la fase `generate-sources` incluya la adición de `target/generated-sources/annotations` a los directorios de fuentes compiladas.

2. **Garantizar Compatibilidad ByteBuddy / Java 25 & Zero-Mockito**:
   - Confirmar la presencia de `-Dnet.bytebuddy.experimental=true` y `--enable-preview` en el `<argLine>` de `maven-surefire-plugin` y `maven-failsafe-plugin` en `pom.xml`.
   - Para pruebas unitarias de dominio (`domain/`), mantener el uso de stubs in-memory y Java 25 records puros conforme a la directiva Zero-Mockito.

3. **Garantizar Estabilidad de Spring `ApplicationContext`**:
   - Al generarse correctamente las implementaciones `TaxiCallerMapperImpl`, `HbxMapperImpl`, `SyncStateMapperImpl`, `BookingMappingMapperImpl`, etc., los beans de Spring `@Component` se instanciarán correctamente en la suite de integración `@SpringBootTest`, eliminando la caída en cascada.

4. **Secuencia de Compilación Definitiva**:
   - Para construir y probar de forma determinista el backend Java:
     ```bash
     cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java
     ./mvnw clean test
     ```
   - El resultado garantizado es `BUILD SUCCESS` con `Tests run: 274, Failures: 0, Errors: 0, Skipped: 0`.

---

## 5. Verification Method

Para verificar independientemente que la remediación resuelve al 100% las fallas:

### 1. Verificación del Backend Java (`services/backend-java`)
```bash
cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java
./mvnw clean test
```
**Criterio de Invalidación**: Si la salida no finaliza en `BUILD SUCCESS` con `Tests run: 274, Failures: 0, Errors: 0`.

### 2. Verificación del Frontend / BFF Go (`services/bff-go`)
```bash
cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/bff-go
go test -v ./...
```
**Criterio de Invalidación**: Si algún test de Go falla (debe indicar `PASS` para los 10 tests).
