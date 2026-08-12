## 2026-07-29T18:02:22Z
Eres el Implementador (Worker M3 Gen 2) para el Hito 3: Optimización de pctMultiMicroservices.

Tu directorio de trabajo asignado es: /home/jaruiz/Desarrollo/.agents/worker_m3_gen2
El repositorio a modificar es: /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices
Lee el informe de handoff en: /home/jaruiz/Desarrollo/.agents/explorer_m3/handoff.md

OBJETIVOS DE IMPLEMENTACIÓN:
1. Esquema gRPC / Protobuf v3:
   - Crear los archivos proto en `proto/pct/v1/`: `booking_service.proto`, `telemetry_service.proto` y `tenant_service.proto`.
   - Generar código gRPC en Go (`services/bff-go/gen/proto/pct/v1/`) y Java (`services/backend-java/`).
   - Implementar el servidor gRPC Netty en Java Backend (puerto 9090) sobre Virtual Threads con interceptores de metadatos `X-Tenant-ID` y W3C `traceparent`.
   - Implementar el pool de clientes gRPC reutilizables en Go BFF (`services/bff-go/grpc_client.go`).
2. Optimización con `sync.Pool` en Go BFF:
   - Crear `services/bff-go/pools.go` (`byteBufferPool`, `gpsTelemetryPool`, `telemetryBatchPool`).
   - Refactorizar `handlers.go` y `proxy.go` para reutilizar buffers de memoria en webhooks de telemetría y respuestas JSON.
3. Arquitectura de Persistencia Segregada:
   - Capa Caliente (Redis): Implementar en Go (`go-redis/v9`) y Java (`spring-boot-starter-data-redis`) para posiciones GPS activas, bloqueos distribuidos atómicos (`SET NX` sustituyendo `syncLocks` de Firestore) y caché de respuestas CQRS.
   - Capa Fría (Firestore): Mantener como almacenamiento duradero maestro de entidades finalizadas e historial de trazado con patrón Write-Behind asíncrono.
4. Compilar y ejecutar pruebas (`go test ./...` en `services/bff-go` y `mvn clean test` en `services/backend-java`) documentando comandos y resultados exactos.

AVISO OBLIGATORIO DE INTEGRIDAD:
DO NOT CHEAT. All implementations must be genuine. DO NOT hardcode test results, create dummy/facade implementations, or circumvent the intended task. A Forensic Auditor will independently verify your work. Integrity violations WILL be detected and your work WILL be rejected.

ENTREGABLES:
- Escribe tu informe de cambios y resultados de pruebas en /home/jaruiz/Desarrollo/.agents/worker_m3_gen2/handoff.md.
- Actualiza /home/jaruiz/Desarrollo/.agents/worker_m3_gen2/progress.md.
- Notifica al orquestador al terminar.
