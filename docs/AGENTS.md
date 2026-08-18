# AGENTS.md - Ecosistema Corporativo de Desarrollo Agéntico & Universidad Privada (12 Facultades)

Este archivo define la arquitectura agéntica, las reglas de orquestación y el mapeo implícito de intenciones a skills para todos los subagentes y proyectos bajo `/home/jaruiz/Desarrollo`.

## 1. Mapeo de Intención a Skill (SDLC Automated Dispatch 6-Phase)

Cada vez que se reciba un requerimiento u objetivo, el agente invocará incondicionalmente las skills correspondientes:

- **Nueva funcionalidad o módulo (SDLC 6-Phase Pipeline)** → `formal_verification_architect` / `spec-driven-development` (DEFINE) → `planning-and-task-breakdown` (PLAN) → `incremental-implementation` (BUILD) → `test-driven-development` / `zero-mockito-tdd-engineer` (VERIFY) → `code-review-and-quality` / `consilium_romano_architect` (REVIEW) → `shipping-and-launch` / `slsa-sigstore-release-sentinel` (SHIP)
  > *Nota de Armonización SDLC*: Las skills canónicas de `GEMINI.md` (`test-driven-development`, `code-review-and-quality`) definen el marco metodológico general de 6 fases, mientras que las skills especializadas del ecosistema (`zero-mockito-tdd-engineer`, `consilium_romano_architect`) implementan las reglas estrictas de dominio puro sin mocks y el tribunal dialéctico pre-merge.
- **Creación de Nuevo Proyecto / Vertical** → Script: `scripts/scaffolding/create_enterprise_project.py`
- **Verificación Formal y Demostración de Teoremas** → `formal_verification_architect` (Lean 4, Z3 SMT, Interpretación Abstracta)
- **Ingesta Masiva, Web Scraping & Streaming ETL** → `web-scraping-and-ingestion-engineer` → `streaming_etl_architect` → `sre_finops_auditor`
- **Compilación AOT & Leyden CDS** → `leyden-aot-build-master`
- **Diseño de APIs (REST / gRPC)** → `api-and-interface-design`
- **Trabajo de UI / Dashboard / PWA / Browser-Use** → `frontend-ui-engineering` → `browser-testing-with-devtools` → `web-scraping-and-ingestion-engineer`
- **Bugs, infracciones o tracebacks** → `debugging-and-error-recovery` (Auto-fix: `scripts/consilium_romano_tribunal.py --auto-fix`)
- **Simulación y Gemelo Digital** → `digital-twin-enkf-orchestrator` → `simulation-telemetry-sqlite-analyzer`
- **Base de Conocimiento y Grounding (Crawl4AI & MarkItDown)** → Script: `scripts/auto_university_rag_sync.py` → `web-scraping-and-ingestion-engineer`
- **Documentación & ADRs** → `adr-knowledge-graph-curator` (`codebase-memory-mcp`)
- **Auditoría Pre-Merge & Senatus Consultum (`/ship`)** → `scripts/consilium_romano_tribunal.py` (`@deepseek-r1`, `@qwen2.5-coder`, `@gemma3:4b`)
- **Seguridad, SLSA L3 y Firmas Cosign** → `slsa-sigstore-release-sentinel`
- **Despliegue y Release** → `shipping-and-launch` → `slsa-sigstore-release-sentinel`

---

## 2. Matriz de Especialización por Proyecto

### A. `corp-spring-boot-starter` & `pctMultiMicroservices` (Java 25 / Spring Boot 4.1 / Go 1.26)
* Delega en la skill `corp-go-high-throughput-expert` y las de backend Java.

### B. `SaaSRegantes` (Cloud Run / Firestore / BigQuery / React Multi-Tenant)
* Delega en las skills de arquitectura cloud-native.

### C. `AppViajes` (Flutter / Movilidad H3 / OSRM)
* Delega en la skill `corp-flutter-mobility-expert`.

### D. `Verticales y Core Engines`
* **Grounded Javadoc Obligatorio**: `@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md`, `@see docs/formacion_ecosistema/BIBLIOGRAFIA_ACADEMICA.md` y `@see docs/formacion_ecosistema/METODO_FEYNMAN_GUIA_PEDAGOGICA.md`.

---

## 3. Reglas de Orquestación, MCPs y los 3 Meta-Bucles

1. **No Chaperones & Lean Dispatch**: Los subagentes son ejecutados de forma directa sin metapersonas intermedias, adoptando skills hiper-especializadas bajo demanda.
2. **Review Fan-Out y Senatus Consultum (`/ship`)**:
   - Invocación obligatoria del tribunal **Consilium Romano 3.0** (`scripts/consilium_romano_tribunal.py`).
   - Magistrados locales en oposición dialéctica: `deepseek-r1:8b` (Inquisitor / CoT / Hoare Logic), `qwen2.5-coder:7b` (Censor Morum / DDD / Java 25 Loom) y `gemma3:4b` (Praetor FinOps / Myerson / SRE).
   - **Rúbrica Feynman & Citas Fundacionales**: Toda resolución del tribunal debe contrastarse contra las 49 fuentes primarias (Shannon, Lamport, Raft, Codd, Hoare, Drepper) y garantizar la ausencia de jerga defensiva.
3. **Integración Profunda con Servidores MCP**:
   - `codebase-memory-mcp`: Grafo de conocimiento y trazabilidad de dependencias entre componentes.
   - `sqlite-mcp-server`: Registro de auditorías y proveniencia en `simulations_telemetry.db` (tablas `consilium_romano_audits`, `paper_ingestion_catalog` y `university_knowledge_nodes`).
   - `docker-mcp-server`: Gestión de contenedores y emuladores locales.
4. **Los 3 Meta-Bucles de Ejecución**:
   - **Meta-Bucle 1 (Code & Build)**: AST Gatekeeper + AOT Leyden CDS + Zero-Mockito TDD + SLSA L3 Cosign pre-merge.
   - **Meta-Bucle 2 (Audit & ADR)**: Consilium Romano 3.0 adversarial + RAG grounding + Sagas/Stripe Idempotence + Core Web Vitals (INP < 200ms).
   - **Meta-Bucle 3 (Master Twin Loop)**: Asimilación EnKF con convergencia de covarianza (< 0.50) + Uber H3 + LiteRT INT8 quantization.
5. **Prove-It Standard**: Ninguna corrección de error ni nueva característica se da por terminada sin un test verde comprobado ejecutable.
6. **Lex Streaming Data**: Ingesta analítica desacoplada mediante micro-batching O(1) y BigQuery con particionamiento forzoso.
7. **Lex Observability & Zero-PII**: Logs estructurados W3C con `traceId` y `tenantId` inyectados en MDC/ScopedValue y ofuscación activa (`ZeroPiiMaskingConverter`).
8. **Lex PubSub Resilience**: Batching settings (250 msgs / 10ms / 512KB), exponential backoff con full jitter y Dead Letter Queues (`*-dlq`).
