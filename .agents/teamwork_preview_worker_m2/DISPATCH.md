## 2026-08-09T11:34:53Z
Task dispatch for Worker 2 (teamwork_preview_worker_m2).
Target: Project 2 (pctMultiMicroservices at /home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices)

Execute auto-repairs and verification steps:
1. Go BFF Fix: services/bff-go/mcp_wasm_host/mcp_wasm_host.go:22, replace []wasmtime.Val{} with nil or []wasmtime.AsExtern{}.
2. Frontend Vitest Fix: services/frontend/package.json (or frontend/package.json), add "@testing-library/dom": "^10.4.0" to devDependencies. Run tests.
3. Hexagonal Purity Script Fix: scripts/validate_hexagonal_purity.py, update domain_dir path.
4. TaxiCaller Script Fix: Add import requests to test_taxicaller.py.
5. Java Null Safety Fix: services/backend-java/src/main/java/com/pct/integracion/application/service/PricingService.java:238, add null guard for docRef.set(cacheData).
6. Java Backend Test Verification: Run ./mvnw clean test in services/backend-java.
