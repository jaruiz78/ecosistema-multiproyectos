# Project: Corporate Multi-Project Audit, Auto-Repair & Zero-Cost Testing

## Architecture
- **corp-spring-boot-starter**: Core library (`com.corp.tenant:corp-spring-boot-starter:1.0.0`) providing multi-tenancy, ScopedValues, structured concurrency, and ArchUnit DDD hexagonal domain enforcement.
- **pctMultiMicroservices**: Microservices monorepo (`backend-java` Spring Boot 4.1 / Java 25, `bff-go` Go 1.25, `frontend` React 19 / Vitest).
- **SaaSRegantes**: 13-module multi-tenant irrigation platform (Java 25, Spring Boot 4.1, Next.js 16 / React 19 PWA & Dashboard).
- **AppViajes**: Mobility ecosystem (`backend-api` Java 25, `fraud-shield-api` Go, `frontend-web` React 19, `mobile-app` Flutter/Dart).
- **Master Digital Twin**: Unified Neural ODE / H3 / EnKF simulation engine (`master_digital_twin.py`, `tensor_gnn_core.py`, `pinn_surrogate_et0.py`).

## Feature Inventory
| # | Feature / Scope | Description | Milestone | Source |
|---|-----------------|-------------|-----------|--------|
| 1 | Core Starter Artifact & ArchUnit | Install `corp-spring-boot-starter` to local .m2 and verify DDD purity | M1 | survey_1 |
| 2 | `pctMultiMicroservices` Java Backend | Spring Boot 4.1 / Java 25 backend tests (274 tests) & ArchUnit validation | M2 | survey_2 |
| 3 | `pctMultiMicroservices` Go BFF Fix | Repair `mcp_wasm_host.go` `wasmtime.NewInstance` type error (`[]AsExtern`) | M2 | survey_2 |
| 4 | `pctMultiMicroservices` Frontend Fix | Add `@testing-library/dom` to `devDependencies` and verify Vitest execution | M2 | survey_2 |
| 5 | `pctMultiMicroservices` Scripts Repair | Fix `validate_hexagonal_purity.py` path and `test_taxicaller.py` import | M2 | survey_2 |
| 6 | SaaSRegantes 13-Module Build & Test | Build and run tests across all 13 modules using Zero-Cost GCP stubs | M3 | survey_3 |
| 7 | Master Digital Twin Optimization | Make `master_digital_twin.py` sleep configurable and fix `run_full_prod_simulation_benchmark.py` import | M3 | survey_3 |
| 8 | Digital Twin Clean Execution | Verify clean execution of `python3 master_digital_twin.py` with exit code 0 | M3 | survey_3 |
| 9 | `AppViajes` Backend API Build & Test | Build & test `AppViajes/services/backend-api` against installed starter artifact | M4 | survey_1 |
| 10 | `AppViajes` Go & Multi-Service Verification | Run Go test suite for `fraud-shield-api` and verify frontend/mobile/bot readiness | M4 | survey_1 |

## Code Layout
- `/home/jaruiz/Desarrollo/corp-spring-boot-starter/`
- `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/`
  - `services/backend-java/`
  - `services/bff-go/`
  - `services/frontend/`
  - `scripts/`
- `/home/jaruiz/Desarrollo/SaaSRegantes/`
  - 13 Maven modules (`module-shared`, `module-infrastructure`, ..., `module-boot`)
  - `frontend/dashboard/`, `frontend/farmer-pwa/`
  - `scripts/`, `simulation/`
- `/home/jaruiz/Desarrollo/AppViajes/`
  - `services/backend-api/`
  - `services/fraud-shield-api/`
  - `services/frontend-web/`
  - `services/mobile-app/`
  - `services/reddit-bot/`

## Interface Contracts
- `corp-spring-boot-starter-1.0.0.jar` published to `~/.m2/repository/com/corp/tenant/corp-spring-boot-starter/1.0.0/`
- `master_digital_twin.py`: CLI invocation `python3 master_digital_twin.py [N_TICKS]` returns exit code 0. `TWIN_SLEEP_SEC` env var controls tick delay.
- `bff-go`: `go build ./...` and `go test ./...` pass with code 0.

## Milestones
| # | Name | Scope | Dependencies | Status |
|---|------|-------|-------------|--------|
| M1 | Core Starter & ArchUnit | Build and install `corp-spring-boot-starter` artifact to local Maven repo; verify DDD ArchUnit rules | none | DONE |
| M2 | `pctMultiMicroservices` Auto-Repair & Test Suite | Fix Go WASM host type error, Frontend test dependency, Python scripts, and verify Java backend tests | M1 | DONE |
| M3 | SaaSRegantes & Master Digital Twin | Optimize twin sleep time, fix benchmark import, run 13 SaaSRegantes Maven modules and Digital Twin simulation | M1 | DONE |
| M4 | `AppViajes` Multi-Service Verification | Build and test Java backend API, Go fraud shield API, and verify Flutter/React frontend readiness | M1, M2 | DONE |
