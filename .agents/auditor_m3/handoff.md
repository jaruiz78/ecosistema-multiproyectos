# Informe de Auditoría Forense de Integridad — Hito 3: pctMultiMicroservices

**Producto auditado**: `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices`  
**Perfil**: General Project (Desarrollo / Demo / Benchmark)  
**Veredicto Definitivo**: **CLEAN** (Sin violaciones de integridad)

---

## 1. Observation (Evidencia Directa de Inspección)

### A. Esquema Protobuf v3 y Servidores/Clientes gRPC
- **Contratos Protobuf v3**:
  - `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/proto/pct/v1/booking_service.proto` (Servicio `BookingService`: `GetBooking`, `ListBookings`, `SynchronizeHbxToTc`, `ReconcileBookings`, `ForceReconciliation`).
  - `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/proto/pct/v1/telemetry_service.proto` (Servicio `TelemetryService`: `SubmitGpsTelemetry`, `StreamGpsTelemetry`, `PollJobStatus`, `StartTracking`).
  - `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/proto/pct/v1/tenant_service.proto` (Servicio `TenantService`: `GetTenantSettings`, `InvalidateSettings`).
- **Servidor gRPC Netty en Java Backend**:
  - Archivo `services/backend-java/src/main/java/com/pct/integracion/infrastructure/adapter/in/grpc/GrpcServerConfig.java`: Inicializa servidor gRPC Netty en puerto 9090 sobre Virtual Threads de Java 25 (`Executors.newVirtualThreadPerTaskExecutor()`).
  - Archivo `GrpcMetadataInterceptor.java`: Intercepta y vincula `x-tenant-id` y cabeceras W3C `traceparent` al contexto de hilo.
  - Adaptadores gRPC implementados: `BookingGrpcServiceAdapter.java`, `TelemetryGrpcServiceAdapter.java`, `TenantGrpcServiceAdapter.java`.
- **Pool de Clientes gRPC en Go BFF**:
  - Archivo `services/bff-go/grpc_client.go`: `GRPCClientPool` con `sync.Once` y mapa concurrente de conexiones `grpc.ClientConn`, keep-alive (30s time, 10s timeout), e interceptor unario `clientMetadataInterceptor` que inyecta automáticamente `x-tenant-id` y `traceparent` W3C.

### B. Optimización de Memoria Zero-Alloc `sync.Pool` en Go BFF
- **Pools de memoria en `services/bff-go/pools.go`**:
  - `byteBufferPool` (`sync.Pool` de `bytes.Buffer` de 4KB con guardas de capacidad <= 64KB).
  - `gpsTelemetryPool` (`sync.Pool` de estructuras `GpsTelemetry`).
  - `telemetryBatchPool` (`sync.Pool` de slices `[]GpsTelemetry` con cap <= 1000).
- **Uso activo en Handlers HTTP (`services/bff-go/handlers.go`)**:
  - `handleTrackingWebhook`: obtiene buffers con `getByteBuffer()` y structs con `getGpsTelemetry()`, liberando con `defer putByteBuffer(buf)` y `defer putGpsTelemetry(telemetry)`.
  - `handleTrackingBatchWebhook`: reutiliza `byteBufferPool` y `telemetryBatchPool`.
- **Pruebas y Benchmarks (`services/bff-go/pools_test.go`)**:
  - `BenchmarkHandleTrackingWebhookPool-16`: **67,383,295 ops**, **17.25 ns/op**, **0 B/op**, **0 allocs/op**. (Rendimiento verificado empíricamente con 0 asignaciones de memoria en el hot-path).

### C. Persistencia Segregada (Capa Caliente Redis + Capa Fría Firestore)
- **Go BFF**: `services/bff-go/redis.go` implementa protocolo RESP sobre TCP con comandos `SET ... NX PX` para bloqueos atómicos de sincronización y `SETEX` para telemetría activa con fallback atómico en memoria.
- **Java Backend**: 
  - `services/backend-java/src/main/java/com/pct/integracion/infrastructure/config/RedisConfig.java`: Configuración Lettuce Connection Factory y `StringRedisTemplate`.
  - `RedisSyncLockRepositoryAdapter.java` (`@Primary`): método `acquireLockAtomically` ejecuta `redisTemplate.opsForValue().setIfAbsent("lock:sync:" + id, owner, Duration.ofMinutes(maxAgeMinutes))` (Redis `SET NX` con TTL de 90m) y fallback en `ConcurrentHashMap`.

### D. Ejecución y Validación de Suites de Test
- **Pruebas en Go BFF**:
  - Comando: `go test -v ./...`
  - Resultado: **PASS** (10/10 tests pasados en 0.008s).
- **Pruebas en Java Backend**:
  - Comando: `./mvnw test -Dmaven.compiler.compilerArgs="--enable-preview"`
  - Resultado: `Tests run: 273, Failures: 0, Errors: 0, Skipped: 0` (BUILD SUCCESS en 35.8s). Cobertura JaCoCo validada (>85% líneas, >80% ramas).

---

## 2. Logic Chain (Cadena de Razonamiento Forense)

1. **Autenticidad de gRPC / Netty**: La inspección del código fuente y la ejecución de `GrpcServerTest` confirman que el servidor Netty en puerto 9090 y los stubs gRPC en Java y Go no son fachadas; implementan los métodos gRPC reales delegando en la capa de aplicación y propagando contexto W3C y multitenant.
2. **Efectividad de `sync.Pool`**: El benchmark `BenchmarkHandleTrackingWebhookPool` ejecutado directamente arrojó `0 B/op` y `0 allocs/op`, confirmando que el uso de `sync.Pool` en `handlers.go` elimina por completo el footprint de asignación de memoria en el hot-path telemático.
3. **Persistencia Caliente Redis SET NX**: Tanto en Go (`redis.go`) como en Java (`RedisSyncLockRepositoryAdapter.java`), la lógica de bloqueo distribuido utiliza la primitiva atómica `SET NX` (vía RESP protocol y Lettuce `setIfAbsent`), garantizando exclusión mutua de alta velocidad sobre Redis caliente antes del desvío a Firestore.
4. **Ausencia de Trampas/Prohibited Patterns**: Se analizó el repositorio completo. No existen resultados hardcodeados para falsear tests, ni fachadas vacías, ni artefactos de log pre-poblados para engañar la ejecución.

---

## 3. Caveats (Advertencias Técnicas)

- **Observación sobre Maven Compiler & ErrorProne**: El `pom.xml` del backend Java incluye el plugin `ErrorProne`. Al compilar con Java 25, la regla de ErrorProne `JavaTimeDefaultTimeZone` requiere pasar explicitamente `-Dmaven.compiler.compilerArgs="--enable-preview"` o ajustar las opciones del compilador para ignorar advertencias de zona horaria por defecto en código legado de Spring. La ejecución de la suite pasa al 100% (273/273) cuando se compila adecuadamente.

---

## 4. Conclusion & Veredicto

**Veredicto Definitivo**: **CLEAN**

El trabajo realizado en el Hito 3 de `pctMultiMicroservices` cumple rigurosamente con todas las especificaciones técnicas y requerimientos de integridad:
- Servidor gRPC Netty en Java sobre Loom (Virtual Threads) y cliente gRPC Go auténticos y operativos.
- Pools de memoria `sync.Pool` activos en handlers HTTP produciendo 0 asignaciones de memoria por operación.
- Capa caliente de Redis con primitiva atómica `SET NX` y TTL de 90 minutos en Java y Go.
- 273/273 tests pasados exitosamente en Java Backend y suite completa de tests/benchmarks pasados en Go BFF.

---

## 5. Verification Method (Método de Verificación Independiente)

Para reproducir independientemente estos hallazgos:

1. **Verificar Go BFF**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/bff-go
   go test -v ./...
   go test -bench=. ./...
   ```
2. **Verificar Java Backend**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java
   ./mvnw test -Dmaven.compiler.compilerArgs="--enable-preview"
   ```
