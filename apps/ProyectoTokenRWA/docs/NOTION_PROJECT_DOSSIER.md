# Notion Project Dossier: ProyectoTokenRWA (Tokenización de Activos Reales RWA y Yield)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-tokenrwa-v1`
* **Líder Técnico / Arquitecto**: `@Stripe-Fintech-Engineer` & `@Zero-Trust-Security-Auditor`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$15,00 USD/mes`** (\(\approx \$0,00290\text{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, BigQuery ML, Merkle Trees, Escrow Inmutable.

---

## 2. Visión General y Objetivos de Negocio
Fraccionamiento y tokenización de activos de infraestructura física (huertos solares, redes de riego, inmuebles logísticos) con liquidación automatizada de rendimientos (Yield APY).
* **KPI Precisión Predicción Yield APY (wMAPE)**: **`0,22%`** (\(R^2 = 0,9960\)).
* **KPI Idempotencia Transaccional**: 100% de liquidaciones garantizadas sin fallos de doble cobro.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[RWA-101]** Modelo BQML para estimación y proyección de dividendos/APY en base a producción física.
- [x] **[ESCROW-102]** Sistema de custodia inmutable y liquidación mediante `core-govtech-ledger`.
- [x] **[ETL-103]** Ingesta desacoplada de eventos de inversión y traspasos.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-004](file:///home/jaruiz/Desarrollo/docs/adr/adr-004-zero-trust-beyondcorp-slsa.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
