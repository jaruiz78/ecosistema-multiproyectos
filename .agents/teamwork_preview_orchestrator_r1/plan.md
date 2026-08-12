# Master Plan: Corporate Multi-Project Audit, Auto-Repair & Zero-Cost Verification

## Phase 0: Survey & Initial Investigation
- Dispatch 3 parallel Explorer agents to survey the 4 corporate projects:
  - `explorer_appviajes`: AppViajes (Flutter / H3 / OSRM)
  - `explorer_pct_backend`: pctMultiMicroservices & corp-spring-boot-starter (Java 25, Spring Boot 4.0, Virtual Threads, DDD Hexagonal, Go workers)
  - `explorer_saas_twin`: SaaSRegantes & Master Digital Twin (GCP, Cloud Run, Firestore, BigQuery, Neural ODEs/H3, master_digital_twin.py)
- Consolidate findings into `PROJECT.md` (Feature Inventory, Architecture, Interface Contracts, Code Layout, Milestones).

## Phase 1: Decomposition & Milestone Definition
- Define milestone boundaries:
  - M1: `corp-spring-boot-starter` & core DDD Hexagonal domain/starter modules
  - M2: `pctMultiMicroservices` microservices & Go workers
  - M3: `SaaSRegantes` Multi-Tenant GCP backend & Master Digital Twin (`master_digital_twin.py`)
  - M4: `AppViajes` Flutter application & H3 mobility services
- Establish GCP Zero-Cost guidelines (Testcontainers, dry-runs, mocks).

## Phase 2: Iterative Execution & Auto-Repair
For each milestone:
1. Dispatch Explorers for detailed bug/lint/build investigation.
2. Dispatch Worker to implement fixes & execute local build/test commands.
3. Dispatch Reviewers for 5-axis code quality & architecture review.
4. Dispatch Challengers for stress & edge-case verification.
5. Dispatch Forensic Auditor (`teamwork_preview_auditor`) for anti-cheating & integrity checks.
6. Evaluate Gate Result in `GATE_STATUS.md`.

## Phase 3: Final Integration & E2E Validation
- Ensure 100% test pass rate across Java (`mvn clean compile`, `mvn test`), Go (`go build`, `go test`), Flutter (`flutter build`, `flutter test`), and Python (`python3 master_digital_twin.py` exit code 0).
- Verify Zero-Cost GCP compliance.
- Report victory to Sentinel.
