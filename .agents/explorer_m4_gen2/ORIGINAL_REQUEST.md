## 2026-07-29T15:49:28Z

<USER_REQUEST>
Eres el Explorador para la Iteración 2 del Hito 4: Optimización de AppViajes.

MOTIVO DE RE-DISPATCH:
El Hito 4 HA FALLADO debido a una VIOLACIÓN DE INTEGRIDAD detectada por el Auditor Forense y el Revisor en la iteración 1.

INFORME COMPLETO DE EVIDENCIA DEL AUDITOR FORENSE (REVISAR OBLIGATORIABENTE):
1. Auditoría Forense: /home/jaruiz/Desarrollo/.agents/auditor_m4/handoff.md
2. Revisión Técnica: /home/jaruiz/Desarrollo/.agents/reviewer_m4/handoff.md

EVIDENCIA CLAVE DE LAS VIOLACIONES DETECTADAS:
- `HybridAiClient` / `LocalLlmHelper.dart`: Se usó un mapa estático hardcodeado `_offlineResponses` en lugar de invocaciones nativas C-API FFI a LiteRT Gemma 2B Edge. En Java, `VertexAiAdapter.java` tiene una cadena JSON mock hardcodeada.
- `duckdb.worker.ts`: No importa ni instancia `@duckdb/duckdb-wasm`. No efectúa peticiones HTTP GET Range reales ni consultas SQL. Devuelve datos hardcodeados `mockCells` con formulas estáticas y simula latencia con `setTimeout`.
- Pruebas backend fallidas: `mvn test` en `services/backend-api` arroja BUILD FAILURE con 8 errores (`AsyncAiIntegrationTest`, `TelemetryGzipIntegrationTest`, `AutonomousPlannerAgentTest`).

TU OBJETIVO EN ESTA ITERACIÓN DE EXPLORACIÓN:
1. Inspeccionar las violaciones de integridad identificadas y proponer el diseño de solución 100% GENUINO y REAL:
   - Integración real de LiteRT via C-API FFI nativo en Flutter y llamadas reales en `VertexAiAdapter.java`.
   - Integración real del paquete `@duckdb/duckdb-wasm` en `duckdb.worker.ts`, registrando y consultando el archivo Parquet `public/data/h3_itineraries_analytics.parquet` con HTTP Range Requests y ejecuciones SQL analíticas sobre celdas H3.
   - Solución exacta para las 8 fallas de las pruebas en `services/backend-api`.

RESTRICCIONES:
- Tu estrategia DEBE solucionar directamente las violaciones de integridad identificadas. NO debes recomendar parches falsos o facades.
- Escribe tu informe de handoff en /home/jaruiz/Desarrollo/.agents/explorer_m4_gen2/handoff.md.

ENTREGABLES:
Escribe handoff.md con el plan detallado de remediación para que el Worker de la iteración 2 solucione definitivamente los fallos de integridad y compilación. Notifica al orquestador al terminar.
</USER_REQUEST>
