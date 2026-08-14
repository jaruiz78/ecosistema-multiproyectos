# Notion Project Dossier: ProyectoCircular (Economía Circular y Trazabilidad de Residuos)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-circular-v1`
* **Líder Técnico / Arquitecto**: `@Java-Spring-Expert` & `@Zero-Trust-Security-Auditor`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `MEDIA (P3)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$18,50 USD/mes`** (\(\approx \$0,00380\text{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, BigQuery ML, Merkle Traceability.

---

## 2. Visión General y Objetivos de Negocio
Plataforma de auditoría, trazabilidad de cadenas de reciclaje de plástico y biomasa, y cálculo de balances de masa circulares.
* **KPI Precisión Predicción Reciclaje (wMAPE)**: **`1,75%`** (\(R^2 = 0,9919\)).
* **KPI Integridad de Cadena de Custodia**: 100% de lotes firmados criptográficamente.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[CIRC-101]** Modelo BQML para predicción de volumen de reciclaje semanal por planta de valorización.
- [x] **[MERKLE-102]** Generación de pasaportes digitales de producto con Merkle Trees de `core-govtech-ledger`.
- [x] **[ETL-103]** Ingesta streaming de pesajes de báscula mediante `UnifiedStreamingEtlPipeline`.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-008](file:///home/jaruiz/Desarrollo/docs/adr/adr-008-slsa-l3-sigstore-provenance.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
