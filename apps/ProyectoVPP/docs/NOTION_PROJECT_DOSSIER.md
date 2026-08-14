# Notion Project Dossier: ProyectoVPP (Virtual Power Plant & Mercados de Flexibilidad)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-vpp-v1`
* **Líder Técnico / Arquitecto**: `@Unified-Twin-Architect` & `@Java-Spring-Expert`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$28,00 USD/mes`**
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, PyPSA, BQML, EnKF Asimilación de Red.

---

## 2. Visión General y Objetivos de Negocio
Agregación de recursos energéticos distribuidos (baterías, cargadores de VE, aerogeneradores) para participar en mercados de ajuste y servicios de balance de la red eléctrica.
* **KPI Tiempo de Respuesta de Balance**: **`< 100 ms`** ante órdenes de regulación de frecuencia.
* **KPI Disponibilidad SLA**: **`99,9997%`**.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[VPP-101]** Agregación de capacidad flexible en mallas H3 para alivio de congestión en subestaciones.
- [x] **[VPP-102]** Ingesta de telemetría de inversores y baterías mediante `UnifiedStreamingEtlPipeline`.
- [x] **[VPP-103]** Sincronización continua con el Gemelo Digital `core-kalman-twin`.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-003](file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
