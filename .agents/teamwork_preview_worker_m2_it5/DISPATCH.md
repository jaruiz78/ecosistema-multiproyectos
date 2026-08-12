## 2026-08-09T10:15:42Z
You are a teamwork_preview_worker operating in working directory `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it5/`.

Your task is to execute the complete remediation for Milestone 2 (`pctMultiMicroservices`):

1. Read context and strategy files:
   - `/home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it5_1/handoff.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it5_3/handoff.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_auditor_m2_it4/handoff.md`

2. Fix all 20 ErrorProne compilation violations in `services/backend-java`:
   - `ReconcileCancellationsService.java`: add `ZoneId.of("UTC")` or `ZoneOffset.UTC` for date/time calls.
   - `ReconcileDriverChangesService.java`: add `ZoneId.of("UTC")`.
   - `ReconcileNewBookingService.java`: remove unused variable `table` / `mapping`, add `ZoneId.of("UTC")`.
   - `RetryFailedBookingsService.java`: add `ZoneId.of("UTC")`.
   - `RouteFraudDetectionService.java`: add `ZoneId.of("UTC")`.
   - `SlaAlertService.java`: add `ZoneId.of("UTC")`.
   - `TenantContext.java`: fix `StringSplitter` and `StringCaseLocaleUsage` (`Locale.ROOT`).
   - `LiteRtAiAdapter.java`: fix `DefaultCharset` (`StandardCharsets.UTF_8`) and `MathAbsoluteNegative`.
   - `EmulatorSeeder.java`: fix `NarrowCalculation` and replace `java.util.Date` with `java.time.Instant`.
   - `FirestoreBookingMappingRepositoryAdapter.java`: remove unused variable, assign/check `Future` return value, replace `java.util.Date`.
   - `FirestoreSyncLockRepositoryAdapter.java`: replace `java.util.Date` with `java.time.Instant`.

3. Configure `services/backend-java/pom.xml`:
   - Ensure compiler arguments handle ErrorProne checks cleanly.
   - Ensure surefire plugin `<argLine>` includes `-XX:+EnableDynamicAgentLoading --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED`.

4. Build & Verify:
   - Run `mvn clean install -DskipTests` in `/home/jaruiz/Desarrollo/corp-spring-boot-starter`.
   - In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`, run `./mvnw clean compile` (verify `BUILD SUCCESS`, 0 compilation errors).
   - Run `./mvnw clean test` (verify `BUILD SUCCESS`, 274/274 tests pass green).
   - In `services/bff-go`, run `go test ./...`.
   - In `services/frontend`, run `npm test`.
   - In `scripts`, run `python3 validate_hexagonal_purity.py`.

5. Write `handoff.md` in `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it5/handoff.md` with complete, honest execution logs and output excerpts.
6. Send a message to parent (`f9371416-a9e5-4082-a76e-ea41cf8e9a2d`) notifying completion.
