## 2026-08-09T10:05:20Z

You are a teamwork_preview_worker operating in working directory `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it4/`.

Your task is to execute the 4-phase remediation plan for Milestone 2 (`pctMultiMicroservices`):

1. Read context and strategy files:
   - `/home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it4_2/handoff.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_auditor_m2_it3/handoff.md`

2. Phase 1 — Fix ErrorProne violations in `services/backend-java`:
   - `src/main/java/com/pct/integracion/infrastructure/adapter/out/ai/LiteRtAiAdapter.java`
   - `src/main/java/com/pct/integracion/infrastructure/adapter/out/ai/VertexAiAdapter.java`
   - `src/main/java/com/pct/integracion/infrastructure/adapter/out/analytics/BigQueryAnalyticsAdapter.java`
   - `src/main/java/com/pct/integracion/infrastructure/adapter/out/analytics/BigQueryAnalyticsQueryAdapter.java`
   Fix all String capitalization Locale rules (e.g. `String.toLowerCase(Locale.ROOT)`), ZoneId parameters (`LocalDate.now(ZoneId.of("UTC"))`), Future return value assignments, unused variables/methods, and `@Override` annotations.

3. Phase 2 — Maven & gRPC Configuration:
   - Check `services/backend-java/pom.xml`. Ensure `build-helper-maven-plugin` registers generated sources (`target/generated-sources/protobuf/java`, `grpc-java`, `annotations`).
   - Ensure surefire/failsafe plugin `<argLine>` includes `--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED`.

4. Phase 3 — Remove Facade Test:
   - Delete fake stub test file `services/backend-java/src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java`.

5. Phase 4 — Compilation & Verification:
   - In `/home/jaruiz/Desarrollo/corp-spring-boot-starter`, run `mvn clean install -DskipTests`.
   - In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`, run `./mvnw clean compile` and verify compilation succeeds (`BUILD SUCCESS`).
   - Run `./mvnw clean test` and verify all 274 tests pass green with 0 errors and 0 failures.
   - In `services/bff-go`, run `go test ./...`.
   - In `services/frontend`, run `npm test`.
   - In `scripts`, run `python3 validate_hexagonal_purity.py`.

6. Write `handoff.md` in `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it4/handoff.md` documenting all code changes made, compilation output, and test results.
7. Send a message to parent (`f9371416-a9e5-4082-a76e-ea41cf8e9a2d`) notifying completion.
