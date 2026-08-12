## 2026-08-09T10:35:29Z
You are a teamwork_preview_worker operating in working directory `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it7/`.

Your task is to execute the definitive fix for `pctMultiMicroservices/services/backend-java` (Milestone 2):

1. Read context and strategy files:
   - `/home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it7_3/handoff.md`
   - `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it7_2/handoff.md`

2. Edit `services/backend-java/pom.xml`:
   - Remove `<arg>--should-stop=ifError=FLOW</arg>` from compiler arguments.
   - Add explicit `-Xep` warning overrides to `<compilerArgs>`: `-Xep:FutureReturnValueIgnored:WARN`, `-Xep:StringCaseLocaleUsage:WARN`, `-Xep:UnusedMethod:WARN`, `-Xep:StringSplitter:WARN`, `-Xep:JavaTimeDefaultTimeZone:WARN`, `-Xep:JavaUtilDate:WARN`, `-Xep:DefaultCharset:WARN`, `-Xep:MathAbsoluteNegative:WARN`, `-Xep:NarrowCalculation:WARN`.

3. Fix ErrorProne source violations in the 11 Java files in `services/backend-java`:
   - `GcpPubSubCacheInvalidator.java` & `LocalTaskSchedulerAdapter.java` (`FutureReturnValueIgnored`)
   - `SecretManagerAdapter.java`, `PredictiveFleetService.java`, `LocalSecretAdapter.java`, `TaxiCallerMapper.java` (`StringCaseLocaleUsage` -> `Locale.ROOT`)
   - `TcAuthManager.java` (`UnusedMethod`)
   - `TenantContext.java` & `GetNewBookingsService.java` (`StringSplitter`)
   - `GetNewBookingsService.java`, `ProcessAssignmentEventService.java`, `ReconcileCancelBookingService.java` (`JavaTimeDefaultTimeZone` -> `ZoneOffset.UTC` / `ZoneId.of("UTC")`)

4. Build & Verify:
   - In `/home/jaruiz/Desarrollo/corp-spring-boot-starter`, run `mvn clean install -DskipTests`.
   - In `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`, run `./mvnw clean test` (verify `BUILD SUCCESS` and 0 test failures/errors).
   - In `services/bff-go`, run `go test ./...`.
   - In `services/frontend`, run `npm test`.
   - In `scripts`, run `python3 validate_hexagonal_purity.py`.

5. Write `handoff.md` in `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it7/handoff.md`.
6. Send a message to parent (`f9371416-a9e5-4082-a76e-ea41cf8e9a2d`) notifying completion.
