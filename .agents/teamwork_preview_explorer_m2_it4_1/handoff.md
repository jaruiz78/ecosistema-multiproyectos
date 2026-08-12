# Investigation Handoff Report — Milestone 2 (`pctMultiMicroservices/services/backend-java`)

**Working Directory**: `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it4_1`  
**Target Project**: `pctMultiMicroservices/services/backend-java`  
**Parent Conversation ID**: `f9371416-a9e5-4082-a76e-ea41cf8e9a2d`  
**Date**: 2026-08-09  

---

## 1. Observation

### Observation 1: Root Cause Categorization of Backend-Java Build/Test Failures

A deep forensic inspection of `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java` revealed five distinct root cause categories responsible for compilation failures, build errors, and the 75 test failures/errors identified during audit execution:

#### Category A: ErrorProne & Java 25 Static Compiler Compliance Errors
1. **`LiteRtAiAdapter.java:123`**: Calling `LocalDate.now()` violates `JavaTimeDefaultTimeZone` because it silently uses the system default time-zone.
2. **`VertexAiAdapter.java:122`**: Calling `String#toLowerCase()` violates `StringCaseLocaleUsage` (requires explicit `Locale.ROOT` or `Locale.getDefault()`).
3. **`VertexAiAdapter.java:148`**: `predictDelay` implements `AiPredictionPort` method without `@Override` annotation (`MissingOverride`).
4. **`BigQueryAnalyticsAdapter.java:283, 406, 444, 710, 766`**: Ignoring returned `Future` from `virtualThreadExecutor.submit(...)` violates `FutureReturnValueIgnored`.
5. **`BigQueryAnalyticsAdapter.java:526, 527, 528, 607, 608, 609, 769`**: Calling `String#toLowerCase()` without explicit `Locale` violates `StringCaseLocaleUsage`.
6. **`BigQueryAnalyticsQueryAdapter.java:24`**: Unused field `tableName` violates `UnusedVariable`.

#### Category B: Maven Build Lifecycle & Spring Boot AOT Interference
1. In `pom.xml` (lines 543-547), `spring-boot-maven-plugin` executes `process-aot` during standard compilation/test phases.
2. Spring Boot AOT processing (`process-aot`) inspects Spring components and rewrites/deletes class structures in `target/classes/`. When `compiler:testCompile` executes after `process-aot`, javac encounters missing inner class files (e.g., `NoSuchFileException: target/classes/.../TaxiCallerOrderDto$MetaRoute.class` and `OpenMeteoClient.class`).
3. Protobuf generation (`protobuf-maven-plugin`) generates gRPC classes (`BookingServiceGrpc.java`, `TelemetryServiceGrpc.java`, `TenantServiceGrpc.java`) into `target/generated-sources/protobuf/grpc-java` and `java`. If clean test is invoked without source root binding or prior `generate-sources` phase execution, test compilation fails with missing symbol errors for gRPC classes.

#### Category C: MapStruct Mapper Generation & Classloading
1. `HbxMapper`, `TaxiCallerWebhookMapper`, `BookingMappingMapper`, `SyncStateMapper`, `JobSyncStatusMapper`, and `TaxiCallerMapper` use `@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)` and pom-level `-Amapstruct.defaultComponentModel=spring`.
2. When unit tests attempt to load mappers using `Mappers.getMapper(HbxMapper.class)` or reflection without a Spring ApplicationContext, MapStruct throws `ClassNotFoundException: Cannot find implementation for com.pct.integracion.infrastructure.adapter.out.hbx.mapper.HbxMapper`.

#### Category D: Mockito & ByteBuddy Dynamic Reflection Under Java 25
1. Java 25 preview mode (`--enable-preview`) enforces strict encapsulation and dynamic agent loading restrictions (`-XX:+EnableDynamicAgentLoading`).
2. Tests such as `MultiProviderRoutingTest` fail at `Mockito.mock(HbxClient.class)` with `Mockito cannot mock this class` or `IllegalStateException: Failed to load ApplicationContext` because Surefire's `<argLine>` lacks explicit Java 25 agent dynamic loading and module export opens flags (`-XX:+EnableDynamicAgentLoading --add-opens=java.base/java.lang=ALL-UNNAMED`).

#### Category E: Hardcoded Facade Test Stubs (Integrity Violation)
1. File: `src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java`.
2. Code:
```java
class FirestoreCostModelTest {
    @Test
    void stubTest() {
        // Simple test to confirm functionality
        assertTrue(true);
    }
}
```
3. Finding: No `FirestoreCostModel` production class exists in `src/main/java`. This test file is a hardcoded stub returning `assertTrue(true)`, violating integrity rules against facade implementations and hardcoded test assertions.

---

## 2. Logic Chain

1. **Prerequisite Verification**: `corp-spring-boot-starter` must be built first (`mvn clean install -DskipTests`) so `corp-spring-boot-starter-1.0.0.jar` is available in `~/.m2`. (Verified: built successfully, exit code 0).
2. **Static Compilation Analysis**: Running `./mvnw compile` succeeds when ErrorProne errors are treated as warnings (`-XepAllErrorsAsWarnings`), but emits explicit warnings for `LiteRtAiAdapter`, `VertexAiAdapter`, `BigQueryAnalyticsAdapter`, and `BigQueryAnalyticsQueryAdapter`. Under strict compliance mode, these static violations halt build pipelines.
3. **Lifecycle & Test Compilation Breakdown**: When `./mvnw clean test` runs, `spring-boot-maven-plugin:process-aot` triggers during class processing. AOT mutation of `target/classes` causes javac during `testCompile` to fail with `NoSuchFileException` on inner record classes (`TaxiCallerOrderDto$MetaRoute.class`, `OpenMeteoClient.class`).
4. **Test Runtime Failure Cascade**:
   - Lack of `-XX:+EnableDynamicAgentLoading` and `--add-opens` in `maven-surefire-plugin` `<argLine>` prevents ByteBuddy from creating dynamic mocks on Java 25, failing tests like `MultiProviderRoutingTest`.
   - Direct invocation of Spring-managed MapStruct mappers via `Mappers.getMapper()` fails in unit test contexts without Spring context or explicit generated implementation references.
5. **Integrity & Quality Violation**: `FirestoreCostModelTest.java` contains `assertTrue(true)` for a non-existent production class, creating false test metrics.

---

## 3. Caveats

- **External Services**: All testing must maintain Zero Cloud Cost compliance using mocks, Testcontainers, or local emulators. No real GCP API calls or BigQuery billing operations may be executed.
- **Other Modules Verified**: BFF Go (`services/bff-go`), React Frontend (`frontend`), and Hexagonal Domain Purity scanner (`scripts/validate_hexagonal_purity.py`) build and pass all tests green. Remediation is strictly scoped to `services/backend-java`.

---

## 4. Conclusion

The build and test failures in `services/backend-java` stem from static ErrorProne violations, improper Maven lifecycle plugin executions (`process-aot` during `compile`), missing Java 25 JVM flags in Surefire (`-XX:+EnableDynamicAgentLoading`), MapStruct instantiation in non-Spring tests, and fake test stubs (`FirestoreCostModelTest.java`).

All failures can be resolved cleanly without shortcuts or fake tests by implementing the step-by-step remediation strategy detailed below.

---

## 5. Remediation Strategy for Worker

The Worker agent must execute the following concrete, step-by-step remediation plan in order:

### Step 1: Pre-build Dependency Installation
- Run `mvn clean install -DskipTests` in `/home/jaruiz/Desarrollo/corp-spring-boot-starter` to populate `~/.m2/repository`.

### Step 2: Fix Static Compiler & ErrorProne Compliance Errors
Modify source files in `pctMultiMicroservices/services/backend-java/src/main/java`:
1. **`VertexAiAdapter.java`**:
   - Line 122: Replace `tenant.toLowerCase()` with `tenant.toLowerCase(Locale.ROOT)`.
   - Line 148: Add `@Override` annotation above `predictDelay(...)`.
2. **`LiteRtAiAdapter.java`**:
   - Line 123: Replace `LocalDate.now()` with `LocalDate.now(ZoneId.systemDefault())`.
3. **`BigQueryAnalyticsAdapter.java`**:
   - Lines 283, 406, 444, 710, 766: Assign `virtualThreadExecutor.submit(...)` return value to `var unused = ...` or annotate with `@SuppressWarnings("FutureReturnValueIgnored")`.
   - Lines 526, 527, 528, 607, 608, 609, 769: Pass `Locale.ROOT` to all `.toLowerCase()` calls.
4. **`BigQueryAnalyticsQueryAdapter.java`**:
   - Line 24: Remove unused field `tableName`.

### Step 3: Fix Maven Plugin Configuration (`pom.xml`)
In `pctMultiMicroservices/services/backend-java/pom.xml`:
1. **Move `process-aot` Execution**: Restrict `spring-boot-maven-plugin` execution `process-aot` so it does not run during standard `mvn test` phase (e.g. bind to phase `prepare-package` or encapsulate within an `aot` maven profile).
2. **Update Surefire & Failsafe JVM Arguments for Java 25**:
   Update `maven-surefire-plugin` and `maven-failsafe-plugin` `<argLine>` configuration:
   ```xml
   <argLine>@{argLine} --enable-preview -Dnet.bytebuddy.experimental=true --enable-native-access=ALL-UNNAMED -XX:+EnableDynamicAgentLoading --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED</argLine>
   ```
3. **Ensure Generated Sources Source Root Inclusion**: Verify `protobuf-maven-plugin` and `maven-compiler-plugin` configure source roots for both `compile` and `test-compile`.

### Step 4: MapStruct Mapper Test Instantiation Fixes
- For unit tests testing MapStruct mappers or classes depending on mappers without Spring context (e.g., `TaxiCallerMapperTest`, `HbxBookingMapperTest`):
  - Ensure tests instantiate the generated `MapperImpl` directly (e.g. `new HbxMapperImpl()`) or use `Mappers.getMapper(...)` correctly.
  - For Spring integration tests (`@SpringBootTest`), ensure mappers are autowired as `@Autowired private HbxMapper hbxMapper;`.

### Step 5: Remediate Fake / Dummy Test Classes
- Delete `FirestoreCostModelTest.java` (`src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java`).
- If test coverage for Firestore model/adapter is needed, create authentic unit/integration tests for `FirestoreClientResolver` or `BigQueryAnalyticsAdapter` using Testcontainers / local emulator mocks without hardcoded `assertTrue(true)`.

### Step 6: Full Verification Execution
- Run `./mvnw clean test` in `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`.
- Confirm 100% green test execution: `Tests run: 274, Failures: 0, Errors: 0, Skipped: 0` with `BUILD SUCCESS`.

---

## 6. Verification Method

To independently verify this report and strategy:
1. Confirm prerequisite installation: `mvn clean install -DskipTests` in `/home/jaruiz/Desarrollo/corp-spring-boot-starter`.
2. Inspect source locations:
   - `VertexAiAdapter.java` (lines 122, 148)
   - `LiteRtAiAdapter.java` (line 123)
   - `BigQueryAnalyticsAdapter.java` (lines 283, 526)
   - `FirestoreCostModelTest.java`
3. Execute `./mvnw clean test` in `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`.
