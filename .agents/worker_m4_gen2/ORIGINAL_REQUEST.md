## 2026-07-29T15:54:30Z
<USER_REQUEST>
Eres el Implementador de Remediación (Worker) para la Iteración 2 del Hito 4: Optimización de AppViajes.

Tu directorio de trabajo asignado es: /home/jaruiz/Desarrollo/.agents/worker_m4_gen2
El repositorio a modificar es: /home/jaruiz/Desarrollo/AppViajes
Lee el informe de handoff de remediación en: /home/jaruiz/Desarrollo/.agents/explorer_m4_gen2/handoff.md

OBJETIVOS DE REMEDIACIÓN:
1. Remediar Inferencia LiteRT FFI y Vertex AI:
   - Eliminar el mapa estático `_offlineResponses` en `LocalLlmHelper.dart` e implementar bindings `dart:ffi` a la C-API nativa de LiteRT (`TfLiteModelCreateFromFile`, `TfLiteInterpreterCreate`, `TfLiteInterpreterInvoke`, `TfLiteTensorCopyToBuffer`).
   - Eliminar la respuesta JSON mock hardcodeada en `VertexAiAdapter.java` y sustituirla por llamadas generativas genuinas a Vertex AI.
2. Remediar DuckDB-WASM y HTTP GET Range Requests en Frontend Web:
   - Declarar `@duckdb/duckdb-wasm` en `services/frontend-web/package.json`.
   - Refactorizar `duckdb.worker.ts` importando `@duckdb/duckdb-wasm`, instanciando `AsyncDuckDB`, registrando el archivo Parquet `/data/h3_itineraries_analytics.parquet` con `DuckDBDataProtocol.HTTP` para HTTP GET Range Requests reales y ejecutando consultas SQL analíticas sobre celdas H3.
3. Solucionar las 8 fallas de pruebas en el Backend Java (`services/backend-api`):
   - Corregir `AsyncAiIntegrationTest.java` (añadir llamada de warmup MockMvc).
   - Corregir `TelemetryGzipIntegrationTest.java` (stubbear `Storage.signUrl`).
   - Corregir `AutonomousPlannerAgentTest.java` (refactorizar `LlmStub` a `static class TestLlmStub`).
4. Compilar y verificar con `mvn clean test` en `services/backend-api` (logrando un BUILD SUCCESS con 120/120 tests pasados y 0 errores), `flutter test` y `npm run build`.

AVISO OBLIGATORIO DE INTEGRIDAD:
DO NOT CHEAT. All implementations must be genuine. DO NOT hardcode test results, create dummy/facade implementations, or circumvent the intended task. A Forensic Auditor will independently verify your work. Integrity violations WILL be detected and your work WILL be rejected.

ENTREGABLES:
- Escribe tu informe de cambios y resultados de pruebas en /home/jaruiz/Desarrollo/.agents/worker_m4_gen2/handoff.md.
- Actualiza /home/jaruiz/Desarrollo/.agents/worker_m4_gen2/progress.md.
- Notifica al orquestador al terminar.
</USER_REQUEST>
