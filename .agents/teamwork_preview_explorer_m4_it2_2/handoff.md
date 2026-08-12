# Handoff Report — Explorer 2 (`teamwork_preview_explorer_m4_it2_2`)

## 1. Observation

Target project: `AppViajes` (`/home/jaruiz/Desarrollo/AppViajes`)  
Investigation target: Audit failures identified in Milestone 4 (`services/backend-api` test failures, `services/fraud-shield-api` tautological tests, and facade/dummy stub implementations).

### Observation 1.1: `services/backend-api` Maven Test Suite Failures (7 Test Errors)
Running `mvn clean test` in `/home/jaruiz/Desarrollo/AppViajes/services/backend-api` fails with 7 test errors across 4 test classes:
1. `TelemetryGzipIntegrationTest` (3 test errors: `testTelemetryGzipIngestion`, `testUploadUrlEndpoint`, `testItineraryFallbackOnException`):
   - Error: `UnsatisfiedDependencyException: Error creating bean with name 'ai.itinera.backend.TelemetryGzipIntegrationTest': Unsatisfied dependency expressed through field 'telemetryController': No qualifying bean of type 'ai.itinera.backend.infrastructure.adapter.web.TelemetryController' available`.
   - File location: `src/test/java/ai/itinera/backend/TelemetryGzipIntegrationTest.java` line 35.
   - Root cause: `TelemetryController` constructor requires `RescueModeService`, which in turn requires `AlternativeTransportPort`. `TelemetryGzipIntegrationTest` mocks `managementService`, `publisherAdapter`, `dlqService`, and `storage`, but lacks `@MockitoBean private RescueModeService rescueModeService;` or `@MockitoBean private AlternativeTransportPort alternativeTransportPort;`.
2. `DueDiligenceMitigationTest` (1 test error):
   - Error: `NoClassDefFoundError: ai/itinera/backend/domain/port/out/CryptoPolygonPayoutPort`.
   - File location: `src/test/java/ai/itinera/backend/application/service/DueDiligenceMitigationTest.java` lines 9, 87.
   - Root cause: `CryptoPolygonPayoutPort` is defined as an interface in `src/main/java/ai/itinera/backend/domain/port/out/CryptoPolygonPayoutPort.java`, but no `@Component` infrastructure adapter implementing `CryptoPolygonPayoutPort` exists in the application package, causing Spring component scan and bean initialization to fail when running full test suite.
3. `DomainModelTest` (1 test error: `testStableRulesAndValues`) & `OtaStressMonteCarloTest` (2 test errors: `runOtaCompressionMonteCarloSimulation`, `testStableRulesReflectionReset`):
   - Error: `NoClassDefFoundError: Could not initialize class ai.itinera.backend.domain.model.StableRules`.
   - File location: `src/test/java/ai/itinera/backend/domain/model/OtaStressMonteCarloTest.java` lines 17-31, 80-87.
   - Root cause: `OtaStressMonteCarloTest` uses illegal reflection (`getDeclaredField("BOOKING_COMMISSION")` and `getDeclaredField("value")`) to mutate private static final `StableValue` fields inside `StableRules`. In Java 25 / modular JVM environment, reflection on private static fields throws exceptions during JVM class loading, causing `ExceptionInInitializerError` which permanently marks `StableRules` as uninitializable (`NoClassDefFoundError`).

### Observation 1.2: Go `services/fraud-shield-api` Tautological Tests & Missing HTTP Unit Tests
1. **Tautological Assertions in Table Test**:
   - File: `/home/jaruiz/Desarrollo/AppViajes/services/fraud-shield-api/main_test.go` lines 34-35 & 50-56:
     ```go
     expectedSafe: []bool{true, false},
     expectedReasons: []string{"SAFE", "AI_RISK_HIGH"},
     ```
   - In `TestEvaluateRisk_TableDriven`, the variable `safe` is a `bool`. Checking whether `safe` equals any item in `[]bool{true, false}` is unconditionally true for every possible output, rendering the test assertion tautological.
2. **Missing HTTP Unit Tests**:
   - File: `/home/jaruiz/Desarrollo/AppViajes/services/fraud-shield-api/main.go` lines 70-133 defines `proxyHandler` for `/api/v1/billing/gpay`.
   - Currently, `main_test.go` only unit tests `evaluator.EvaluateRisk` and `loadConfig()`, leaving `proxyHandler` and HMAC signature forwarding completely unverified at the HTTP layer.

### Observation 1.3: Facade & Dummy Implementations in Java `backend-api`
1. **Fake Archiving Log in `FirestorePersistenceAdapter.java`**:
   - Lines 756-764:
     ```java
     log.info("[Cold Archiving] Transfiriendo 1,500 entradas antiguas a GCP Coldline Storage (gcs-historical-archive).");
     byte[] dummyData = "mock archived itinerary".getBytes(java.nio.charset.StandardCharsets.UTF_8);
     coldlineStorage.archiveFile("archives/itinerary_old_data.json", dummyData, false);
     ```
   - Logs transfer of 1,500 historical entries while writing a hardcoded 23-byte dummy string without reading any records from memory/database.
2. **Facade Endpoint in `TelemetryController.java`**:
   - Lines 134-145:
     ```java
     @PostMapping("/rage-clicks")
     public ResponseEntity<Map<String, String>> ingestRageClick(@RequestBody TelemetryRageClickEvent event) {
         log.warn("[TelemetryController] 🚨 Rage Click Detectado...");
         // Aquí iría la lógica para guardarlo en BigQuery/Firestore... Simulamos un guardado exitoso:
         return ResponseEntity.ok(Map.of("status", "REGISTERED", ...));
     }
     ```
   - Discards `TelemetryRageClickEvent` payload without publishing to Pub/Sub or enqueueing to DLQ.
3. **Hardcoded FCM Token in `FirebaseCloudMessagingAdapter.java`**:
   - Lines 67-71: Returns hardcoded `"dummy-token-for-" + userId` rather than querying a token repository.

---

## 2. Logic Chain

1. **Test Failure Isolation**:
   - In `TelemetryGzipIntegrationTest`, Spring cannot inject `TelemetryController` because `TelemetryController`'s constructor requires `RescueModeService`. Mocking `RescueModeService` (or its dependency `AlternativeTransportPort`) resolves the `UnsatisfiedDependencyException`.
   - Creating a concrete `@Component` adapter `CryptoPolygonPayoutAdapter` implementing `CryptoPolygonPayoutPort` satisfies Spring context dependency resolution in `DueDiligenceMitigationTest`.
   - Providing a clean, public `resetForTesting()` method on `StableValue` and `StableRules` allows `OtaStressMonteCarloTest` to reset state deterministically without invoking unsafe reflection that triggers `ExceptionInInitializerError` / `NoClassDefFoundError`.
2. **Fraud Shield Test Hardening**:
   - `EvaluateRisk` in `internal/shield/evaluator.go` is completely deterministic: returns `(true, "SAFE")` for <10 requests/min and `(false, "RATE_LIMIT_EXCEEDED")` for >=10 requests/min. Replacing tautological slice checks with scalar equality (`expectedSafe: true`, `expectedReason: "SAFE"`) makes test failures mandatory on regression.
   - Adding `httptest.NewServer` unit tests for `proxyHandler` ensures HTTP routing, header propagation, and `Fraud-Shield-Signature` HMAC generation are verified.
3. **Genuine Infrastructure Implementation**:
   - Updating `archiveOldDataToColdStorage()` in `FirestorePersistenceAdapter` to serialize stored records into JSON bytes and purge archived keys removes fake logging and dummy string writes.
   - Routing `TelemetryRageClickEvent` in `TelemetryController.ingestRageClick()` through `publisherAdapter.publishTelemetryEvent(...)` (with `dlqService` fallback) integrates rage click telemetry into the real event pipeline.

---

## 3. Caveats

- Investigation was performed in read-only mode in accordance with agent archetype rules; no project source files in `/home/jaruiz/Desarrollo/AppViajes/` were modified by this explorer agent.
- `services/fraud-shield-api` currently passes `go build` and `go test`, but its test suite relies on tautological assertions and lacks HTTP proxy handler coverage.

---

## 4. Conclusion

All 3 audit failure areas in Milestone 4 (`AppViajes`) have been fully investigated and mapped to root causes. A concrete 4-step remediation plan has been formulated for execution by Worker.

### Concrete 4-Step Remediation Plan for Worker:

#### Step 1: Remediate Java `services/backend-api` Test Suite Failures (7 Test Errors)
1. **Fix `TelemetryGzipIntegrationTest.java`**:
   - File: `services/backend-api/src/test/java/ai/itinera/backend/TelemetryGzipIntegrationTest.java`
   - Action: Add `@MockitoBean private ai.itinera.backend.application.service.RescueModeService rescueModeService;` to test field annotations (around line 54).
2. **Implement `CryptoPolygonPayoutAdapter.java`**:
   - File: `services/backend-api/src/main/java/ai/itinera/backend/infrastructure/adapter/partner/CryptoPolygonPayoutAdapter.java`
   - Action: Create a `@Component` implementing `CryptoPolygonPayoutPort` that validates Polygon wallet addresses and logs payout execution.
3. **Refactor `StableRules.java` & `StableValue.java` State Resets**:
   - File: `services/backend-api/src/main/java/ai/itinera/backend/domain/model/StableValue.java`
   - Action: Add `public void resetForTesting() { value.set(null); }`.
   - File: `services/backend-api/src/main/java/ai/itinera/backend/domain/model/StableRules.java`
   - Action: Add `public static void resetForTesting() { BOOKING_COMMISSION.resetForTesting(); CIVITATIS_COMMISSION.resetForTesting(); GEOGRAPHIC_METADATA.resetForTesting(); }`.
   - File: `services/backend-api/src/test/java/ai/itinera/backend/domain/model/OtaStressMonteCarloTest.java`
   - Action: Replace reflection calls in `resetStableRules()` with `StableRules.resetForTesting()`.

#### Step 2: Remediate Go `services/fraud-shield-api` Tautological & HTTP Tests
1. **Fix Tautological Assertions**:
   - File: `services/fraud-shield-api/main_test.go`
   - Action: In `TestEvaluateRisk_TableDriven`, update struct fields to `expectedSafe bool` and `expectedReason string`. Replace `[]bool{true, false}` with `true` / `false` and exact reasons (`"SAFE"`, `"RATE_LIMIT_EXCEEDED"`).
2. **Add HTTP Proxy Handler Unit Tests**:
   - File: `services/fraud-shield-api/main_test.go`
   - Action: Add `TestProxyHandler_AllowedAndSigned` using `httptest.NewServer` as a target backend and `httptest.NewRequest` to call `proxyHandler`. Assert HTTP status 200 OK and presence of valid `Fraud-Shield-Signature` header. Add `TestProxyHandler_RateLimitExceeded` (11 requests) asserting 403 Forbidden.

#### Step 3: Replace Facade & Dummy Implementations with Authentic Code
1. **Implement Authentic Cold Storage Archiving in `FirestorePersistenceAdapter.java`**:
   - File: `services/backend-api/src/main/java/ai/itinera/backend/infrastructure/adapter/persistence/FirestorePersistenceAdapter.java`
   - Action: In `archiveOldDataToColdStorage()` (lines 756-764), serialize actual inactive records into a JSON byte array, invoke `coldlineStorage.archiveFile(...)`, purge active records, and log actual archived count.
2. **Implement Genuine Event Processing in `TelemetryController.java`**:
   - File: `services/backend-api/src/main/java/ai/itinera/backend/infrastructure/adapter/web/TelemetryController.java`
   - Action: In `ingestRageClick()` (lines 134-145), construct `AnalyticsEventRequest` with event type `RAGE_CLICK`, publish via `publisherAdapter.publishTelemetryEvent(...)`, and route to `dlqService` on error.
3. **Implement Token Repository in `FirebaseCloudMessagingAdapter.java`**:
   - Action: Replace hardcoded String return in `getUserFcmToken()` with lookup in user token storage map.

#### Step 4: Verification & Zero-Regression Check
1. Run `mvn clean test` in `services/backend-api` — verify 0 errors, 0 failures.
2. Run `go test -v -race ./...` in `services/fraud-shield-api` — verify 100% pass.

---

## 5. Verification Method

To verify these findings and the remediation plan:

1. **Verify Maven Test Errors**:
   Inspect `TelemetryGzipIntegrationTest.java`, `DueDiligenceMitigationTest.java`, and `OtaStressMonteCarloTest.java`. Observe missing beans and private field reflection.
2. **Verify Fraud Shield Tautological Test**:
   Inspect `services/fraud-shield-api/main_test.go` lines 34-35 and 50-56 for `expectedSafe: []bool{true, false}`.
3. **Verify Facade Implementations**:
   Inspect `FirestorePersistenceAdapter.java` lines 756-764 and `TelemetryController.java` lines 134-145.
