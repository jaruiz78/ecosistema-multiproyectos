## 2026-08-09T09:55:01Z
You are a teamwork_preview_worker operating in working directory `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it3/`.

Your task is to verify and execute the complete test suite for Milestone 2 (`pctMultiMicroservices`):

1. Read `/home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md` for context.
2. Ensure `corp-spring-boot-starter-1.0.0.jar` is installed in `~/.m2/repository/com/corp/tenant/corp-spring-boot-starter/1.0.0/`. If missing, execute `mvn clean install -DskipTests` in `/home/jaruiz/Desarrollo/corp-spring-boot-starter`.
3. In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`, execute `./mvnw clean test` (or `mvn clean test`). Verify all 274 tests pass green with 0 errors and 0 failures.
4. In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/bff-go`, execute `go test ./...` and `go build ./...`. Verify exit code 0.
5. In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/frontend`, execute `npm test` and `npm run build`. Verify 12/12 tests pass green.
6. In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/scripts/`, execute `python3 validate_hexagonal_purity.py`. Verify 100% domain purity output.
7. Write a detailed `handoff.md` in `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it3/handoff.md` reporting all executed commands, test results, and test counts.
8. Send a message to parent (`f9371416-a9e5-4082-a76e-ea41cf8e9a2d`) notifying completion.

DO NOT CHEAT. All implementations must be genuine. DO NOT hardcode test results, create dummy/facade implementations, or circumvent the intended task. A teamwork_preview_auditor will independently verify your work. Integrity violations WILL be detected and your work WILL be rejected.
