## 2026-07-29T15:43:22Z
<USER_REQUEST>
Eres el Implementador (Worker) para el Hito 4: Optimización de AppViajes.

Tu directorio de trabajo asignado es: /home/jaruiz/Desarrollo/.agents/worker_m4
El repositorio a modificar es: /home/jaruiz/Desarrollo/AppViajes
Lee el informe de handoff en: /home/jaruiz/Desarrollo/.agents/explorer_m4/handoff.md

OBJETIVOS DE IMPLEMENTACIÓN:
1. Inferencia IA Híbrida Edge/Cloud:
   - Implementar la arquitectura de inferencia híbrida con cliente local LiteRT Gemma 2B Edge y fallback automático/resiliente a Vertex AI Cloud.
   - Configurar la gestión de ciclo de trabajo térmico (`ThermalDutyCycleManager`) en el cliente.
2. Motor OLAP Client-Side (DuckDB-WASM + Parquet H3):
   - Implementar el cliente analítico DuckDB-WASM ejecutándose en Web Worker para consultas locales de itinerarios.
   - Configurar la lectura optimizada de archivos Parquet con Z-Order espacial H3 y acotación de RAM por debajo de 20 MB.
3. Compilar y verificar el código cliente y backend asegurando 0 errores de compilación o tipo.

AVISO OBLIGATORIO DE INTEGRIDAD:
DO NOT CHEAT. All implementations must be genuine. DO NOT hardcode test results, create dummy/facade implementations, or circumvent the intended task. A Forensic Auditor will independently verify your work. Integrity violations WILL be detected and your work WILL be rejected.

ENTREGABLES:
- Escribe tu informe de cambios y resultados de pruebas en /home/jaruiz/Desarrollo/.agents/worker_m4/handoff.md.
- Actualiza /home/jaruiz/Desarrollo/.agents/worker_m4/progress.md.
- Notifica al orquestador al terminar.
</USER_REQUEST>
