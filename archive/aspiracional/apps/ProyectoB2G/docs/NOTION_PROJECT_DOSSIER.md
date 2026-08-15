# Notion Project Dossier: ProyectoB2G (GovTech y Tramitación Ciudadana)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-b2g-v1`
* **Líder Técnico / Arquitecto**: `@Java-Spring-Expert` & `@Zero-Trust-Security-Auditor`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$22,00 USD/mes`** (\(\approx \$0,00420\text{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, BigQuery ML, Zero-Trust BeyondCorp, GovTech Ledger.

---

## 2. Visión General y Objetivos de Negocio
Ventanilla única de gestión administrativa, solicitudes ciudadanas y licencias públicas con garantía de inmutabilidad y aislamiento estricto por municipio.
* **KPI Precisión Demanda Solicitudes (wMAPE)**: **`1,97%`** (\(R^2 = 0,9928\)).
* **KPI Tiempo Medio de Tramitación**: Reducción de 5 días a **`< 3 horas`**.
* **KPI Latencia P99**: **`8,90 ms`**.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[GOV-101]** Modelo BQML para predicción de picos de trámites administrativos municipales.
- [x] **[LEDGER-102]** Registro inmutable de resoluciones administrativas en `core-govtech-ledger`.
- [x] **[SEC-103]** Arquitectura Zero-Trust con validación de identidad eIDAS / Cl@ve.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-004](file:///home/jaruiz/Desarrollo/docs/adr/adr-004-zero-trust-beyondcorp-slsa.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
