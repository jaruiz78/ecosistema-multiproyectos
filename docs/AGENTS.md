# AGENTS.md - Ecosistema Corporativo de Desarrollo Agéntico

Este archivo define la arquitectura agéntica, las reglas de orquestación y el mapeo implícito de intenciones a skills para todos los subagentes y proyectos bajo `/home/jaruiz/Desarrollo`.

## 1. Mapeo de Intención a Skill (SDLC Automated Dispatch)

Cada vez que se reciba un requerimiento u objetivo, el agente invocará incondicionalmente la skill correspondiente:

- **Nueva funcionalidad o módulo** → `spec-driven-development` → `planning-and-task-breakdown` → `incremental-implementation` → `test-driven-development`
- **Ingesta Masiva y Streaming ETL** → `streaming-etl-architect` → `sre-finops-auditor` → `bq-dry-run-optimizer`
- **Diseño de APIs (REST / gRPC)** → `api-and-interface-design`
- **Trabajo de UI / Dashboard / PWA** → `frontend-ui-engineering` → `browser-testing-with-devtools`
- **Bugs, errores o tracebacks** → `debugging-and-error-recovery`
- **Refactorización o simplificación** → `code-simplification` → `doubt-driven-development`
- **Auditoría de código pre-merge** → `code-review-and-quality` (`@code-reviewer`)
- **Seguridad, PII y OWASP** → `security-and-hardening` (`@security-auditor`)
- **Web Performance (LCP / INP / CLS)** → `web-performance-auditor` (Agent)
- **Nube de Puntos / Ingesta / Stubs Local LLM** → `ollama-local-ai-orchestrator`
- **Despliegue y Release** → `shipping-and-launch`

---

## 2. Matriz de Especialización por Proyecto

### A. `corp-spring-boot-starter` & `pctMultiMicroservices` (Java 25 / Spring Boot 4.0 / Go)
* **Compilación & CDS**: `leyden-cds-trainer` + `spring-boot4-native-check` + `source-driven-development`.
* **Streaming ETL**: `streaming-etl-architect` con `UnifiedStreamingEtlPipeline`.
* **Testing Estricto**: `test-driven-development` con `qa_tdd_testcontainers` (Zero Mockito en dominio).
* **Documentación ADR**: `documentation-and-adrs` (registro automático en `docs/adr/`).

### B. `SaaSRegantes` (Cloud Run / Firestore / BigQuery / React Multi-Tenant)
* **Auditoría Web**: `web-performance-auditor` + `browser-testing-with-devtools`.
* **FinOps & Costes**: `bq-dry-run-optimizer` + `multi_tenancy_security_specialist` + `streaming-etl-architect`.

### C. `AppViajes` (Flutter / Movilidad H3 / OSRM)
* **Algoritmos & Rendimiento**: `python-vectorization-optimizer` + `h3-surge-calculator` + `queue-bottleneck-detector` + `streaming-etl-architect`.
* **Desarrollo Seguro**: `incremental-implementation` + `doubt-driven-development`.

---

## 3. Reglas de Orquestación y Fan-Out

1. **No Chaperones**: Los subagentes son ejecutados de forma directa sin metapersonas intermedias.
2. **Review Fan-Out en Lanzamiento (`/ship`)**: Antes de publicar o mezclar cambios estructurales, se lanzan en paralelo:
   - `@code-reviewer`: Inspección en 5 ejes.
   - `@security-auditor`: Análisis de vulnerabilidades y PII.
   - `@test-engineer`: Evaluación de cobertura y pruebas de estrés.
3. **Prove-It Standard**: Ninguna corrección de error ni nueva característica se da por terminada sin un test verde comprobado ejecutable.
4. **Lex Streaming Data (Ley de Ingesta Asíncrona)**: Ingesta analítica desacoplada mediante micro-batching O(1) y BigQuery con particionamiento forzoso.

