# Reporte de Auditoría y Encuesta Técnico-Arquitectónica (Survey Report)
**Proyecto**: `pctMultiMicroservices` (`/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices`)  
**Agente**: Explorer 2 (`teamwork_preview_explorer_survey_2`)  
**Fecha**: 2026-08-09T11:29:30Z  

---

## 1. Observation (Observaciones Directas)

### A. Estructura y Microservicios del Proyecto
- **Servicio Java 25 / Spring Boot 4.0 (`services/backend-java`)**:
  - Archivos: `pom.xml`, `Dockerfile`, `Dockerfile.jvm`, `app.jsa` (Project Leyden CDS archive), `src/main/java/com/pct/integracion/`.
  - Dependencias: Spring Boot 4.1.0-M1 parent, Java 25, Virtual Threads (Loom), Spring Cloud GCP 8.0.5, Testcontainers 2.0.5, JUnit 6.0.3/Jupiter 5, ArchUnit 1.4.2, Resilience4j, Jackson 2.21, OpenTelemetry 1.41.0, gRPC 1.65.1.
- **Servicio Go BFF (`services/bff-go`)**:
  - Archivos: `go.mod`, `go.sum`, `main.go`, `handlers.go`, `mcp_wasm_host/mcp_wasm_host.go`, `Dockerfile`.
  - Dependencias: Go 1.25, `wasmtime-go/v15`, gRPC, Google Cloud Firestore, Firebase Auth, Apache Arrow exporter.
- **Frontend Dashboard (`frontend`)**:
  - Archivos: `package.json`, `vite.config.ts`, `tsconfig.json`, `src/App.test.tsx`.
  - Dependencias: React 19.2.7, Vite 8.1.0, Tailwind CSS 4.0.0, Leaflet, Deck.gl, Vitest 4.1.7.
- **Infraestructura y Scripts (`infra/`, `scripts/`, `simulation/`)**:
  - Manifests GCP CloudBuild (`infra/gcp/cloudbuild/cloudbuild_beta.yaml`), Docker Compose (`infra/docker/docker-compose-simulation.yml`), Terraform (`infra/gcp/terraform/main.tf`).

### B. Pruebas y Compilación Ejecutadas
1. **Compilación y Tests de Java (`services/backend-java`)**:
   - Comando: `./mvnw clean test` en `services/backend-java`
   - Resultado verbatim:
     ```text
     [INFO] Results:
     [INFO] Tests run: 274, Failures: 0, Errors: 0, Skipped: 0
     [INFO] ------------------------------------------------------------------------
     [INFO] BUILD SUCCESS
     [INFO] Total time: 01:04 min
     ```
   - OpenAPI Spec `docs/apis/openapi.json` generado exitosamente mediante `GenerateOpenApiSpecTest`.

2. **Compilación y Tests de Go (`services/bff-go`)**:
   - Comando 1: `go test -v .` en `services/bff-go`
     - Resultado: PASS (14 tests unitarios en el paquete raíz pasaron en verde).
   - Comando 2: `go test ./...` / `go build ./...` en `services/bff-go`
     - Error verbatim:
       ```text
       # bff-go/mcp_wasm_host
       mcp_wasm_host/mcp_wasm_host.go:22:55: cannot use []wasmtime.Val{} (value of type []wasmtime.Val) as []wasmtime.AsExtern value in argument to wasmtime.NewInstance
       FAIL bff-go/mcp_wasm_host [build failed]
       ```

3. **Compilación y Tests de Frontend (`frontend`)**:
   - Comando 1: `npm run build` en `frontend`
     - Resultado: EXIT CODE 0 (`✓ built in 437ms`).
   - Comando 2: `npm test` en `frontend`
     - Error verbatim:
       ```text
       FAIL src/App.test.tsx [ src/App.test.tsx ]
       FAIL src/components/AuditTable.test.tsx
       FAIL src/components/ManualBooking.test.tsx
       FAIL src/components/RadarView.test.tsx
       Error: Cannot find module '@testing-library/dom'
       Require stack:
       - .../frontend/node_modules/@testing-library/react/dist/pure.js
       ```

4. **Verificación de Arquitectura DDD Hexagonal y Zero Mockito**:
   - ArchUnit: En `services/backend-java/src/test/java/com/pct/integracion/ArchitectureTest.java`, los 6 test rules pasaron 100% en verde:
     ```text
     [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 -- in com.pct.integracion.ArchitectureTest
     ```
   - Dominio Puro Java: Cero importaciones de Spring (`org.springframework`), JPA (`jakarta.persistence`), Jackson o GCP en `services/backend-java/src/main/java/com/pct/integracion/domain`.
   - Zero Mockito en Dominio: `services/backend-java/src/test/java/com/pct/integracion/domain` contiene cero importaciones de Mockito (`org.mockito`).

5. **Cumplimiento GCP Zero-Cost**:
   - Los tests de integración Java utilizan Testcontainers (`testcontainers-gcloud`) o clases simuladas in-memory (`BigQueryAnalyticsAdapter`, `GoogleMapsRoutingAdapter`).
   - `cloudbuild_beta.yaml` define `--min-instances 0` y `--max-instances 1` para Cloud Run.

6. **Auditoría SAST y Scripts**:
   - Comando: `python3 scripts/run_sast_audit.py`
   - Resultados:
     - Claves de API GCP hardcodeadas detectadas en `frontend/.env.beta:9`, `frontend/.env.production:12`, `frontend/src/firebase.ts:8`, y artefactos compilados.
     - Credenciales en variables en `scripts/user-management/manage_all.js:41`.
     - `scripts/validate_hexagonal_purity.py` falla con:
       ```text
       ⚠️ El directorio de dominio no existe: /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/src/main/java/com/pct/integracion/domain
       ```
       Debido a que busca en `src/main/java/...` en lugar de `services/backend-java/src/main/java/...`.
   - `test_taxicaller.py` referencia `requests.get` / `requests.post` pero carece de `import requests`.
   - `PricingService.java:238`: `docRef.set(cacheData).get()` invoca `.get()` directamente sin null check previo si el mock de Firestore retorna `null`.

---

## 2. Logic Chain (Cadena de Razonamiento)

1. **Evaluación de Backend Java**:
   - *Observación*: Ejecución de `./mvnw clean test` resultó en `BUILD SUCCESS` con 274/274 tests superados y validación exitosa en ArchUnit de la regla hexagonal (`ArchitectureTest`).
   - *Inferencia*: La capa Java 25 / Spring Boot 4.0 está completamente funcional, libre de errores sintácticos, respeta la separación de capas DDD (Dominio, Aplicación, Infraestructura) y la regla de Zero Mockito en el dominio puro.

2. **Diagnóstico del Fallo de Compilación en Go**:
   - *Observación*: `go test .` en la raíz de `bff-go` pasa todos los tests, pero `go test ./...` o `go build ./...` falla en `services/bff-go/mcp_wasm_host/mcp_wasm_host.go:22`.
   - *Inferencia*: La función `wasmtime.NewInstance` firma `(store, module, []AsExtern)`. Se pasó `[]wasmtime.Val{}`, el cual no implementa la interfaz `AsExtern` en `wasmtime-go/v15`. Reemplazar `[]wasmtime.Val{}` por `nil` o `[]wasmtime.AsExtern{}` resolverá la compilación en todo el árbol Go.

3. **Diagnóstico del Fallo de Tests en Frontend**:
   - *Observación*: `npm run build` genera la build de producción en 437ms sin errores TypeScript, pero `npm test` arroja `Cannot find module '@testing-library/dom'`.
   - *Inferencia*: `@testing-library/react` depende de `@testing-library/dom` como peer/dev dependency. Al faltar `@testing-library/dom` en `package.json`, Vitest falla al cargar el entorno de pruebas de componentes React. Añadir `@testing-library/dom` a `devDependencies` resolverá la suite de tests frontend.

4. **Diagnóstico de Errores en Scripts Auxiliares**:
   - *Observación 1*: `python3 scripts/validate_hexagonal_purity.py` busca el paquete de dominio en `pctMultiMicroservices/src/main/java/...`.
   - *Inferencia 1*: Tras la reestructuración del proyecto en monorepo de microservicios (`services/backend-java`), la ruta base del script quedó desalineada. Actualizar la ruta a `services/backend-java/src/main/java/...` reactivará el validador estático de pureza.
   - *Observación 2*: `test_taxicaller.py` usa `requests.get` en línea 8 sin haber importado `requests`.
   - *Inferencia 2*: Agregar `import requests` al inicio de `test_taxicaller.py` corregirá el `NameError`.

5. **Evaluación de Cumplimiento GCP Zero-Cost**:
   - *Observación*: En `services/backend-java`, `BigQueryAnalyticsAdapter` y `GoogleMapsRoutingAdapter` operan en modo simulación/emulado durante la suite de pruebas. `cloudbuild_beta.yaml` establece `--min-instances 0`.
   - *Inferencia*: El proyecto cumple estrictamente con el principio GCP Zero-Cost durante la ejecución de pruebas.

---

## 3. Caveats (Salvedades y Asunciones)

- **Simulaciones Multi-Year (`run_goal.py`)**: Requieren contenedores Docker de OSRM levantados (`infra/docker/docker-compose-simulation.yml`) y Ollama corriendo localmente para la ejecución de escenarios de 5 años. Se verificaron la sintaxis y los scripts, pero no se ejecutó una simulación completa de 5 años.
- **WASM binary (`mcp_codebase_memory.wasm`)**: `mcp_wasm_host.go` implementa un fallback dry-run cuando el módulo WASM no está en disco.

---

## 4. Conclusion (Conclusión y Objetivos de Auto-Reparación)

El proyecto `pctMultiMicroservices` muestra un alto nivel de madurez arquitectónica y de calidad en su core Java (274/274 tests pasaron en verde, 100% pureza DDD Hexagonal). Sin embargo, existen **6 objetivos concretos de auto-reparación** para lograr el 100% de disposición de compilación y testing:

### Listado de Objetivos de Auto-Reparación (Auto-Repair Targets)
1. **Target 1 (Go)**: Modificar `services/bff-go/mcp_wasm_host/mcp_wasm_host.go:22` para pasar `nil` o `[]wasmtime.AsExtern{}` a `wasmtime.NewInstance`, habilitando `go build ./...` y `go test ./...`.
2. **Target 2 (Frontend)**: Agregar `"@testing-library/dom": "^10.4.0"` a `devDependencies` en `services/frontend/package.json` para corregir la suite de tests en `vitest`.
3. **Target 3 (Script Pureza Hexagonal)**: Ajustar la ruta `domain_dir` en `scripts/validate_hexagonal_purity.py` a `services/backend-java/src/main/java/com/pct/integracion/domain`.
4. **Target 4 (Script TaxiCaller)**: Agregar `import requests` en `test_taxicaller.py`.
5. **Target 5 (Java Null Guard)**: En `services/backend-java/src/main/java/com/pct/integracion/application/service/PricingService.java:238`, añadir comprobación `if (docRef.set(cacheData) != null)` para prevenir `NullPointerException` imprevistos en stubs de pruebas.
6. **Target 6 (GCP SAST Sanitize)**: Mover o parametrizar credenciales expuestas en `scripts/user-management/manage_all.js` y sanitizar variables de entorno en frontend.

---

## 5. Verification Method (Método de Verificación Independiente)

Para verificar independientemente el estado y validar las futuras correcciones:

1. **Verificación Backend Java**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/backend-java
   ./mvnw clean test
   ```
   *Criterio de éxito*: `BUILD SUCCESS`, 274 tests pasados en verde, `ArchitectureTest` 6/6 reglas superadas.

2. **Verificación Go BFF**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/services/bff-go
   go test ./...
   go build ./...
   ```
   *Criterio de éxito*: Compilación y tests completados sin errores en `mcp_wasm_host`.

3. **Verificación Frontend**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/frontend
   npm run build
   npm test
   ```
   *Criterio de éxito*: Build limpia y Vitest corriendo tests sin error de módulo faltante.

4. **Verificación de Scripts**:
   ```bash
   cd /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices
   python3 scripts/validate_hexagonal_purity.py
   python3 scripts/run_sast_audit.py
   ```
   *Criterio de éxito*: `validate_hexagonal_purity.py` detecta 0 violaciones en los archivos de dominio de `backend-java`.
