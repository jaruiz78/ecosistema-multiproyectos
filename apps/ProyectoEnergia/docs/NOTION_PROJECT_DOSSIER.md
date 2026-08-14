# Notion Project Dossier: ProyectoEnergia (Comunidades Energéticas y Generación Solar)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-energia-v1`
* **Líder Técnico / Arquitecto**: `@Unified-Twin-Architect` & `@Java-Spring-Expert`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `MEDIA-ALTA (P2)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$28,00 USD/mes`** (\(\approx \$0,00450\text{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, PyPSA OPF (Optimal Power Flow), BQML ARIMA_PLUS, BigQuery.

---

## 2. Visión General y Objetivos de Negocio
Gestión de autoconsumo colectivo, optimización de inyección a red eléctrica y predicción de generación solar fotovoltaica.
* **KPI Precisión Solar (wMAPE)**: **`7,15%`** (\(R^2 = 0,9917\)).
* **KPI Latencia P99**: **`8,90 ms`**.
* **KPI Disponibilidad SLA**: **`99,9997%`**.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[SOLAR-101]** Modelo BQML `model_energia_solar_mwh_forecast` para predicción de generación horaria.
- [x] **[OPF-102]** Despacho óptimo linealizado de energía en comunidades solares locales.
- [x] **[ETL-103]** Ingesta streaming desacoplada particionada por `DATE(timestamp)`.
- [x] **[FINOPS-104]** Integración de `--cpu-boost` y cuotas serverless controladas.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-010](file:///home/jaruiz/Desarrollo/docs/adr/adr-010-bqml-edge-inference-and-kalman-twin-assimilation.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
