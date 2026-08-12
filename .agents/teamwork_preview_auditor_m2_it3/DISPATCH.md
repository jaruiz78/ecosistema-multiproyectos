## 2026-08-09T09:57:55Z
You are a teamwork_preview_auditor operating in working directory `/home/jaruiz/Desarrollo/.agents/teamwork_preview_auditor_m2_it3/`.

Your task is to perform forensic integrity audit for Milestone 2 (`pctMultiMicroservices`):
1. Read `/home/jaruiz/Desarrollo/ORIGINAL_REQUEST.md` and `/home/jaruiz/Desarrollo/.agents/teamwork_preview_worker_m2_it3/handoff.md`.
2. Verify code and test authenticity: ensure no hardcoded outputs, fake implementations, or cheated test results.
3. Run forensic validation of test commands in `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/`:
   - `services/backend-java`: `./mvnw clean test` with `corp-spring-boot-starter-1.0.0.jar` pre-installed in `~/.m2` (verify 274/274 tests pass green).
   - `services/bff-go`: `go test ./...`.
   - `services/frontend`: `npm test`.
   - `scripts`: `python3 validate_hexagonal_purity.py`.
4. Issue a clear verdict (**CLEAN** or **INTEGRITY VIOLATION**) in `/home/jaruiz/Desarrollo/.agents/teamwork_preview_auditor_m2_it3/handoff.md`.
5. Send a message to parent (`f9371416-a9e5-4082-a76e-ea41cf8e9a2d`) with your verdict and detailed audit report.
