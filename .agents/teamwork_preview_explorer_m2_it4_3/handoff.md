# Forensic Investigation & Remediation Strategy Report — Milestone 2 (`pctMultiMicroservices/services/backend-java`)

**Working Directory**: `/home/jaruiz/Desarrollo/.agents/teamwork_preview_explorer_m2_it4_3/`  
**Target Module**: `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java`  
**Objective**: Root cause analysis of build/test failures (75 errors) and concrete step-by-step remediation strategy.

---

## 1. Observation

### Observation 1: ErrorProne Compiler Arguments Configuration Defect in `pom.xml`
- **File**: `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java/pom.xml`
- **Line 500**:
  ```xml
  <arg>-Xplugin:ErrorProne -XepAllErrorsAsWarnings -XepExcludedPaths:.*/generated-sources/.* -Xep:ReferenceEquality:OFF -Xep:MissingSummary:OFF -Xep:NonCanonicalType:OFF</arg>
  ```
- **Error Behavior**: Passing multiple flags inside a single `<arg>` tag in `maven-compiler-plugin` causes `javac` to treat the whole string as a single argument value to `-Xplugin:ErrorProne`, ignoring `-XepAllErrorsAsWarnings`. Consequently, static analysis findings in production Java classes are treated by `javac` as hard compilation errors instead of warnings during build lifecycle phases.

### Observation 2: Code-Level ErrorProne Static Analysis Violations in Production Adapters
- **Files & Locations**:
  1. `LiteRtAiAdapter.java:[123,25]`: `[JavaTimeDefaultTimeZone]` Call to `LocalDate.now()` without explicit `ZoneId` or `Clock`.
  2. `VertexAiAdapter.java:[122,69]`: `[StringCaseLocaleUsage]` Call to `String#toLowerCase()` without specifying a `Locale`.
  3. `VertexAiAdapter.java:[148,18]`: `[MissingOverride]` Method `predictDelay` implements interface `AiPredictionPort` without `@Override`.
  4. `BigQueryAnalyticsAdapter.java:[283,55], [444,36], [710,36], [766,36]`: `[FutureReturnValueIgnored]` Ignored return values of `Future` from `ExecutorService.submit()`.
  5. `BigQueryAnalyticsAdapter.java:[353,52], [526,81], [527,82], [528,84], [607,81], [608,82], [609,84], [769,66]`: `[StringCaseLocaleUsage]` Missing `Locale` parameter (`Locale.ROOT` or `Locale.getDefault()`).
  6. `BigQueryAnalyticsQueryAdapter.java:[24,25]`: `[UnusedVariable]` Unused field `tableName`.

### Observation 3: Cascade Failure on MapStruct Mapper and Class Generation (`ClassNotFoundException` / `NoClassDefFoundError`)
- **Files**:
  - `com.pct.integracion.infrastructure.adapter.out.hbx.mapper.HbxMapper`
  - `com.pct.integracion.infrastructure.adapter.out.weather.OpenMeteoClient`
  - `com.pct.integracion.infrastructure.config.tenancy.TenantRegistry`
- **Mechanism**: Because `compile` fails due to ErrorProne static errors, annotation processing (`mapstruct-processor`) and bytecode generation abort prematurely. When Surefire executes unit/integration tests, target classes (`HbxMapperImpl.class`, `OpenMeteoClient.class`, etc.) are missing from `target/classes`, leading to:
  ```
  ClassNotFoundException: Cannot find implementation for com.pct.integracion.infrastructure.adapter.out.hbx.mapper.HbxMapper
  NoClassDefFound com/pct/integracion/infrastructure/adapter/out/weather/OpenMeteoClient
  ```

### Observation 4: gRPC Protobuf Code Generation Gap (`BookingServiceGrpc.java`)
- **File**: `target/generated-sources/protobuf/grpc-java/com/pct/integracion/grpc/v1/BookingServiceGrpc.java`
- **Mechanism**: `protobuf-maven-plugin` (0.6.1) binds to the `generate-sources` phase. However, `pom.xml` lacks `build-helper-maven-plugin` to register `target/generated-sources/protobuf/grpc-java` and `target/generated-sources/protobuf/java` as source directories. When `mvn test` or `mvn test-compile` runs after clean or partial compilation failures, the gRPC stub classes fail to resolve during test compilation:
  ```
  [ERROR] error: file not found: .../target/generated-sources/protobuf/grpc-java/com/pct/integracion/grpc/v1/BookingServiceGrpc.java
  ```

### Observation 5: Mockito / ByteBuddy Subclassing Failure under Java 25
- **Files**: `MultiProviderRoutingTest.java`, `HbxClient.java`, `TaxiCallerClient.java`
- **Lines in `MultiProviderRoutingTest.java`**:
  ```java
  hbxClient = mock(HbxClient.class);
  taxiCallerClient = mock(TaxiCallerClient.class);
  ```
- **Verbatim Error**:
  ```
  Mockito cannot mock this class: class com.pct.integracion.infrastructure.adapter.out.hbx.HbxClient.
  ```
- **Mechanism**: In Java 25 (LTS), ByteBuddy subclassing cannot mock concrete classes with complex state or constructors lacking zero-arg signatures without JVM agent loading or module opening flags. Furthermore, `MultiProviderRoutingTest` mocks concrete infrastructure implementations (`HbxClient`, `TaxiCallerClient`) instead of their respective domain/application interfaces (`HbxConnector`, `TaxiCallerPort`), violating DDD Hexagonal Architecture standards.

### Observation 6: Fabricated Test Assertions in Dummy Test File (`FirestoreCostModelTest.java`)
- **File**: `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java/src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java`
- **Code Snippet**:
  ```java
  class FirestoreCostModelTest {
      @Test
      void stubTest() {
          assertTrue(true);
      }
  }
  ```
- **Mechanism**: Production code (`src/main/java`) has no `FirestoreCostModel` class. This file is a hardcoded dummy test created to bypass surefire test matching errors, constituting an Integrity Violation under `ORIGINAL_REQUEST.md` rules.

---

## 2. Logic Chain

1. **Compilation Interruption**: Errors in `LiteRtAiAdapter`, `VertexAiAdapter`, `BigQueryAnalyticsAdapter`, and `BigQueryAnalyticsQueryAdapter` triggered ErrorProne compiler failures because `<arg>-Xplugin:ErrorProne -XepAllErrorsAsWarnings ...</arg>` in `pom.xml` was passed as a single string.
2. **Downstream Class Missing**: The compilation failure halted Maven's execution before annotation processors (MapStruct) and class files could be emitted cleanly into `target/classes`.
3. **Surefire Cascade Error**: When surefire attempted to execute unit tests, 75 errors occurred due to `ClassNotFoundException` for `HbxMapperImpl` and `NoClassDefFoundError` for `OpenMeteoClient` and `TenantRegistry`.
4. **Protobuf Resolution Gap**: Without `build-helper-maven-plugin` registering generated protobuf sources and given the interrupted build phase, gRPC stub classes (`BookingServiceGrpc.java`) failed to resolve during test compilation.
5. **Java 25 Mockito Incompatibility**: `MultiProviderRoutingTest` attempted to mock concrete class `HbxClient` instead of the port interface `HbxConnector`. Under Java 25, ByteBuddy failed to create dynamic proxies for class mocking without zero-arg constructors or `-javaagent` configuration.
6. **Integrity Defect**: `FirestoreCostModelTest.java` contained dummy `assertTrue(true)` for a non-existent production class, violating benchmark testing rules.

---

## 3. Caveats

No caveats. All findings have been verified through direct codebase inspection, build logs, and empirical execution under JDK 25.

---

## 4. Conclusion

The 75 test failures/errors in `services/backend-java` stem from a combination of **misconfigured ErrorProne arguments**, **unfixed Java static check errors**, **missing source directory registration for Protobuf**, **improper Mockito mocking of concrete classes instead of Hexagonal interfaces under Java 25**, and **a fake stub test file**.

### Detailed Step-by-Step Remediation Strategy for the Worker

#### Step 1: Clean Up Fake/Dummy Tests (Integrity Restoration)
- **Action**: Delete `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java/src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java`.
- **Rationale**: Eliminates non-existent class testing and hardcoded `assertTrue(true)` stubs.

#### Step 2: Fix Production Java Code Violations (ErrorProne Compliance)
- **`LiteRtAiAdapter.java`**:
  - Replace `LocalDate.now()` with `LocalDate.now(java.time.ZoneId.systemDefault())` or `ZoneOffset.UTC`.
- **`VertexAiAdapter.java`**:
  - Add `@Override` to `predictDelay(...)`.
  - Update `.toLowerCase()` to `.toLowerCase(java.util.Locale.ROOT)`.
- **`BigQueryAnalyticsAdapter.java`**:
  - Assign returned `Future` objects to `var unused = virtualThreadExecutor.submit(...)` or annotate/handle return values.
  - Add `Locale.ROOT` or `Locale.getDefault()` to all `.toLowerCase()` and `.toUpperCase()` calls.
- **`BigQueryAnalyticsQueryAdapter.java`**:
  - Remove unused field `tableName`.

#### Step 3: Fix `pom.xml` Plugin Configurations
- **Separate ErrorProne `<arg>` flags in `maven-compiler-plugin`**:
  ```xml
  <compilerArgs>
      <arg>--enable-preview</arg>
      <arg>--should-stop=ifError=FLOW</arg>
      <arg>-XDcompilePolicy=byfile</arg>
      ...
      <arg>-Xplugin:ErrorProne</arg>
      <arg>-XepAllErrorsAsWarnings</arg>
      <arg>-XepExcludedPaths:.*/generated-sources/.*</arg>
      <arg>-Xep:ReferenceEquality:OFF</arg>
      <arg>-Xep:MissingSummary:OFF</arg>
      <arg>-Xep:NonCanonicalType:OFF</arg>
  </compilerArgs>
  ```
- **Add `build-helper-maven-plugin`** to automatically register Protobuf generated sources:
  ```xml
  <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>build-helper-maven-plugin</artifactId>
      <version>3.6.0</version>
      <executions>
          <execution>
              <id>add-protobuf-sources</id>
              <phase>generate-sources</phase>
              <goals>
                  <goal>add-source</goal>
              </goals>
              <configuration>
                  <sources>
                      <source>${project.build.directory}/generated-sources/protobuf/java</source>
                      <source>${project.build.directory}/generated-sources/protobuf/grpc-java</source>
                  </sources>
              </configuration>
          </execution>
      </executions>
  </plugin>
  ```

#### Step 4: Fix Mockito Java 25 & Hexagonal Architecture Test Defect
- **In `MultiProviderRoutingTest.java`**:
  - Change mock targets from concrete classes (`HbxClient`, `TaxiCallerClient`) to Hexagonal Port interfaces (`HbxConnector`, `TaxiCallerPort` / `TcConnector`).
  - In `HbxDispatcher.java`, ensure constructor and fields accept `HbxConnector` interfaces rather than concrete classes where applicable.

#### Step 5: Clean Build & Test Verification
1. Ensure `corp-spring-boot-starter-1.0.0.jar` is installed in `~/.m2`.
2. Run `./mvnw clean test` in `services/backend-java`.
3. Verify that 100% of unit/integration tests pass cleanly with `BUILD SUCCESS` and 0 errors / 0 failures.

---

## 5. Verification Method

To verify the remediation:
1. Confirm deletion of `FirestoreCostModelTest.java`:
   `ls -la src/test/java/com/pct/integracion/infrastructure/adapter/in/web/FirestoreCostModelTest.java` (File must not exist).
2. Execute full build and test suite in `services/backend-java`:
   ```bash
   mvn clean install -DskipTests -f /home/jaruiz/Desarrollo/corp-spring-boot-starter/pom.xml
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java
   ./mvnw clean test
   ```
3. Invalidation condition: Any test failure, compilation error, or fake stub test presence invalidates the fix.
