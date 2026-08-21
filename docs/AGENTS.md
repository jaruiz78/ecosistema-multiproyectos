# AGENTS.md - Ecosistema Corporativo de Desarrollo Agéntico & Universidad Privada (12 Facultades)

Este archivo define la arquitectura agéntica, las reglas de orquestación, los Custom Agents de Antigravity 2.0, los Hooks de entorno y el mapeo implícito de intenciones a skills para todos los subagentes y proyectos bajo `/home/jaruiz/Desarrollo`.

## 1. Mapeo de Intención a Skill & Custom Agent (SDLC Automated Dispatch 6-Phase)

Cada vez que se reciba un requerimiento u objetivo, el agente invocará incondicionalmente los Custom Agents y skills correspondientes:

- **Nueva funcionalidad o módulo (SDLC 6-Phase Pipeline)** → `formal_verification_architect` / `spec-driven-development` (DEFINE) → `planning-and-task-breakdown` (PLAN) → `incremental-implementation` (BUILD) → `test-driven-development` / `zero-mockito-tdd-engineer` (VERIFY) → `code-review-and-quality` / `consilium_romano_architect` (REVIEW) → `shipping-and-launch` / `slsa-sigstore-release-sentinel` (SHIP)
  > *Nota de Armonización SDLC*: Las skills canónicas de `GEMINI.md` (`test-driven-development`, `code-review-and-quality`) definen el marco metodológico general de 6 fases, mientras que las skills especializadas del ecosistema (`zero-mockito-tdd-engineer`, `consilium_romano_architect`) implementan las reglas estrictas de dominio puro sin mocks y el tribunal dialéctico pre-merge.
- **Backend Java 25 / Spring Boot 4.1** → Custom Agent: `java-spring-expert` (`leyden-aot-build-master`, `zero-mockito-tdd-engineer`)
- **Microservicios Go / Spatial Workers** → Custom Agent: `go-gopher-expert` (`corp-go-high-throughput-expert`)
- **Frontend Web / React / Next.js / DuckDB-Wasm** → Custom Agent: `frontend-wizard` (`frontend_expert`, `a11y-debugging`)
- **Mobile Flutter / Uber H3 Movilidad** → Custom Agent: `mobile-mobility-expert` (`corp-flutter-mobility-expert`, `h3-surge-calculator`)
- **Gemelo Digital Unificado / PEPS / EnKF** → Custom Agent: `unified-twin-architect` (`digital-twin-enkf-orchestrator`, `unified-twin-node-injector`)
- **Ciberseguridad Zero-Trust & SLSA L3** → Custom Agent: `zero-trust-security-auditor` (`firebase-security-rules-auditor`, `slsa-sigstore-release-sentinel`)
- **FinOps & Cloud SRE (< 0.015 USD/MAU)** → Custom Agent: `finops-sre-sentinel` (`sre-finops-auditor`, `bq-dry-run-optimizer`)
- **Tribunal Arquitectónico Pre-Merge (`/ship`)** → Custom Agent: `consilium-romano-tribunal` (`scripts/consilium_romano_tribunal.py`)
- **MLOps Model Drift & Recalibración Adaptativa** → Script: `scripts/scheduled_mlops_drift_monitor.py` & Custom Agent: `mlops-drift-sentinel`
- **Criptografía Post-Cuántica (PQC NIST FIPS 204)** → Custom Agent: `pqc-cryptography-auditor` (`services/bff-go/pqc_security.go`)
- **Grounding Académico & NotebookLM** → Script: `scripts/auto_university_rag_sync.py` & `scripts/generate_notebook_dossiers.py`

---

## 2. Catálogo de Custom Agents y Gobernanza de Tokens (Antigravity 2.0)

Ubicación: [`.agents/agents.yaml`](file:///home/jaruiz/Desarrollo/.agents/agents.yaml) y [`.agents/definitions/`](file:///home/jaruiz/Desarrollo/.agents/definitions/).

| Custom Agent ID | Modelo Preferido | Presupuesto Tokens | Ámbito de Herramientas & MCPs |
| :--- | :--- | :--- | :--- |
| `java-spring-expert` | Gemini 3.7 Flash | 120,000 | Filesystem, `codebase-memory-mcp`, `sqlite-mcp-server` |
| `go-gopher-expert` | Gemini 3.7 Flash | 100,000 | Filesystem, `gopls-mcp-server`, `codebase-memory-mcp` |
| `frontend-wizard` | Gemini 3.7 Flash | 100,000 | Filesystem, `chrome-devtools-mcp`, `visualization` |
| `mobile-mobility-expert` | Gemini 3.7 Flash | 100,000 | Filesystem, `dart-mcp-server`, `sqlite-mcp-server` |
| `unified-twin-architect` | Gemini 3.7 Pro | 150,000 | Filesystem, `sqlite-mcp-server`, `visualization` |
| `zero-trust-security-auditor` | Gemini 3.7 Flash | 100,000 | Filesystem, `firebase-mcp-server`, `codebase-memory-mcp` |
| `consilium-romano-tribunal` | Gemini 3.7 Pro | 120,000 | Filesystem, `sqlite-mcp-server`, `codebase-memory-mcp` |
| `finops-sre-sentinel` | Gemini 3.7 Flash | 90,000 | Filesystem, `bigquery`, `google-cloud-monitoring`, `cloudrun` |
| `mlops-drift-sentinel` | Gemini 3.7 Flash | 80,000 | Filesystem, `sqlite-mcp-server`, Cloud Scheduler |
| `pqc-cryptography-auditor`| Gemini 3.7 Flash | 90,000 | Filesystem, `codebase-memory-mcp`, NIST FIPS 204 |

---

## 3. Arquitectura de Hooks de Entorno (Antigravity Hooks 2.0)

Ubicación: [`.agents/hooks.json`](file:///home/jaruiz/Desarrollo/.agents/hooks.json) y [`scripts/hooks/`](file:///home/jaruiz/Desarrollo/scripts/hooks/).

1. **`pre_tool_call` (`scripts/hooks/pre_tool_hook.py`)**:
   - **AST Domain Gatekeeper**: Bloqueo automático e inmediato de frameworks/anotaciones (`@Entity`, `@Service`, Spring, Hibernate, Mockito) en `domain/`.
   - **Command Safety Filter**: Bloqueo de comandos destructivos (`rm -rf /`, `mkfs`, fork-bombs).
   - **BigQuery Safety Check**: Bloqueo de queries sin filtro forzoso de partición (`_PARTITIONDATE`).
   - **Zero-PII Enforcer**: Bloqueo de logging de credenciales o tokens en claro.
2. **`post_tool_call` (`scripts/hooks/post_tool_hook.py`)**:
   - **Telemetría Instantánea**: Inserción en `simulations_telemetry.db` (tabla `agent_tool_telemetry`).
   - **Auto-Formatting & Linting**: Aseguramiento de saltos de línea y estructura limpia post-edición.
3. **`on_session_start` / `on_session_finish` (`scripts/hooks/session_lifecycle_hook.py`)**:
   - Resumen FinOps de la sesión, cómputo de costes y generación de atestación criptográfica SLSA L3.

---

## 4. Reglas de Orquestación, MCPs y los 3 Meta-Bucles

1. **No Chaperones & Lean Dispatch**: Los subagentes son ejecutados de forma directa sin metapersonas intermedias, adoptando skills hiper-especializadas bajo demanda.
2. **Review Fan-Out y Senatus Consultum (`/ship`)**:
   - Invocación obligatoria del tribunal **Consilium Romano 3.0** (`scripts/consilium_romano_tribunal.py`).
   - Magistrados locales en oposición dialéctica: `deepseek-r1:8b` (Inquisitor / CoT / Hoare Logic), `qwen2.5-coder:7b` (Censor Morum / DDD / Java 25 Loom) y `gemma3:4b` (Praetor FinOps / Myerson / SRE).
   - **Rúbrica Feynman & Citas Fundacionales**: Toda resolución del tribunal debe contrastarse contra las 49 fuentes primarias (Shannon, Lamport, Raft, Codd, Hoare, Drepper) y garantizar la ausencia de jerga defensiva.
3. **Integración Profunda con Servidores MCP**:
   - `codebase-memory-mcp`: Grafo de conocimiento y trazabilidad de dependencias entre componentes.
   - `sqlite-mcp-server`: Registro de auditorías y proveniencia en `simulations_telemetry.db` (tablas `consilium_romano_audits`, `agent_tool_telemetry`, `agent_session_summaries`, `university_knowledge_nodes`).
   - `docker-mcp-server`: Gestión de contenedores y emuladores locales.
4. **Los 3 Meta-Bucles de Ejecución**:
   - **Meta-Bucle 1 (Code & Build)**: AST Gatekeeper + AOT Leyden CDS + Zero-Mockito TDD + SLSA L3 Cosign pre-merge.
   - **Meta-Bucle 2 (Audit & ADR)**: Consilium Romano 3.0 adversarial + RAG grounding + Sagas/Stripe Idempotence + Core Web Vitals (INP < 200ms).
   - **Meta-Bucle 3 (Master Twin Loop)**: Asimilación EnKF con convergencia de covarianza (< 0.50) + Uber H3 + LiteRT INT8 quantization.
5. **Prove-It Standard**: Ninguna corrección de error ni nueva característica se da por terminada sin un test verde comprobado ejecutable.
6. **Lex Streaming Data**: Ingesta analítica desacoplada mediante micro-batching O(1) y BigQuery con particionamiento forzoso.
7. **Lex Observability & Zero-PII**: Logs estructurados W3C con `traceId` y `tenantId` inyectados en MDC/ScopedValue y ofuscación activa (`ZeroPiiMaskingConverter`).
8. **Lex PubSub Resilience**: Batching settings (250 msgs / 10ms / 512KB), exponential backoff con full jitter y Dead Letter Queues (`*-dlq`).
