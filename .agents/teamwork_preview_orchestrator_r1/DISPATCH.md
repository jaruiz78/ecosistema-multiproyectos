# Dispatch Instructions

## 2026-08-09T11:26:45Z

<USER_REQUEST>
Eres el Project Orchestrator (teamwork_preview_orchestrator).
Tu directorio de trabajo es: /home/jaruiz/Desarrollo/.agents/teamwork_preview_orchestrator_r1/
El directorio raíz del espacio de trabajo es: /home/jaruiz/Desarrollo/

Lee las instrucciones del usuario en /home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md.

Mapeo de proyectos corporativos en /home/jaruiz/Desarrollo/:
1. AppViajes (/home/jaruiz/Desarrollo/AppViajes)
2. pctMultiMicroservices (/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices)
3. SaaSRegantes (/home/jaruiz/Desarrollo/SaaSRegantes)
4. corp-spring-boot-starter (/home/jaruiz/Desarrollo/corp-spring-boot-starter)

Tus objetivos:
1. Diseñar el plan de tareas en plan.md dentro de tu directorio de trabajo.
2. Desplegar especialistas para auditar código, arquitectura (ej. DDD Hexagonal), artefactos (dockerfiles, manifiestos GCP, scripts del Gemelo Digital Neural ODEs/H3).
3. Asegurar cumplimiento de Prevención de Costes GCP (Zero Cost: Testcontainers, dry-runs, mocks).
4. Auto-reparar de manera autónoma cualquier error detectado.
5. Ejecutar recompilaciones y test suites en verde para los 4 proyectos (`mvn clean compile` / `mvn test`, `go build` / `go test`, `flutter build` / `flutter test`, y ejecución limpia de simulaciones Python `python3 master_digital_twin.py` con exit code 0).
6. Mantener actualizado /home/jaruiz/Desarrollo/.agents/teamwork_preview_orchestrator_r1/progress.md.
7. Cuando todos los milestones estén completados con éxito y todos los tests estén en verde, reporta la victoria final a Sentinel.
</USER_REQUEST>

## 2026-08-09T11:54:30Z

<USER_REQUEST>
Resume work at /home/jaruiz/Desarrollo/.agents/teamwork_preview_orchestrator_r1/. Read handoff.md, BRIEFING.md, ORIGINAL_REQUEST.md, DISPATCH.md, PROJECT.md, GATE_STATUS.md, and progress.md for current state.
Your parent is f4aa5c3d-cbac-44e2-9148-c0c8484a9ceb — use this ID for all escalation and status reporting (send_message).

Your concrete next steps:
1. Complete Milestone 2 (pctMultiMicroservices): Dispatch an Explorer/Worker to verify `./mvnw clean test` in `services/backend-java` with `corp-spring-boot-starter-1.0.0.jar` pre-installed, verify 274/274 tests pass green, and rerun Reviewers/Challengers/Auditor to achieve Gate PASS for M2.
2. Execute Milestone 3 (SaaSRegantes & Master Digital Twin):
   - Make `master_digital_twin.py` tick sleep configurable via `TWIN_SLEEP_SEC` env var.
   - Fix `run_full_prod_simulation_benchmark.py` `fastapi` import.
   - Run `mvn clean test` across all 13 modules of `SaaSRegantes`.
   - Run `python3 master_digital_twin.py 2` with exit code 0.
   - Run Gate verification (Reviewers, Challengers, Auditor).
3. Execute Milestone 4 (AppViajes):
   - Run `mvn clean test` in `services/backend-api` against installed `corp-spring-boot-starter`.
   - Run `go test ./...` in `services/fraud-shield-api`.
   - Run Gate verification.
4. Report final victory to Sentinel (`f4aa5c3d-cbac-44e2-9148-c0c8484a9ceb`).
</USER_REQUEST>

## 2026-08-09T10:40:59Z

<SENTINEL_MESSAGE>
Hola Orchestrator, soy Sentinel. Liveness Check: por favor actualiza tu progress.md con los últimos avances de M2/M3 y confirma el estado actual.
</SENTINEL_MESSAGE>

## 2026-08-09T13:30:30Z

<SENTINEL_MESSAGE>
Hola Orchestrator, soy Sentinel. Liveness Check: por favor actualiza tu progress.md con los avances completados de M3 y el estado de M4.
</SENTINEL_MESSAGE>

## 2026-08-09T14:08:49Z

<SENTINEL_MESSAGE>
Hola Orchestrator, soy Sentinel. Liveness Check: por favor actualiza tu progress.md con la finalización de M3 y el estado de M4.
</SENTINEL_MESSAGE>

## 2026-08-09T20:38:23Z

<USER_REQUEST>
Resume work at /home/jaruiz/Desarrollo/.agents/teamwork_preview_orchestrator_r1. Read handoff.md, BRIEFING.md, ORIGINAL_REQUEST.md, DISPATCH.md, and progress.md for current state.
Your parent is f4aa5c3d-cbac-44e2-9148-c0c8484a9ceb — use this ID for all escalation and status reporting (send_message).

Your immediate objective:
1. Start your heartbeat cron via schedule(CronExpression="*/10 * * * *").
2. Dispatch Explorer M4 Iteration 3 (teamwork_preview_explorer) to /home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m4_it3/ to analyze the 2 backend-api test failure root causes in AppViajes/services/backend-api: AsyncAiIntegrationTest (missing UgcVideoService$GpsPoint class) and TelemetryGzipIntegrationTest (missing TelemetryController Spring bean).
3. Dispatch Worker M4 Iteration 3 (teamwork_preview_worker) to apply the fixes and verify `mvn clean test`.
4. Dispatch 2 Reviewers, 2 Challengers, and 1 Forensic Auditor for M4 Iteration 3 verification.
5. Upon M4 Gate PASS, mark M4 DONE in PROJECT.md and output the final victory summary.
</USER_REQUEST>
