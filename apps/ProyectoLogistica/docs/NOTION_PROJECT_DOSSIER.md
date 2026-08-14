# Notion Project Dossier: ProyectoLogistica (VRP Estocástico y Última Milla)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-logistica-v1`
* **Líder Técnico / Arquitecto**: `@Go-Gopher` & `@Java-Spring-Expert`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región de Despliegue**: GCP Cloud Run (`europe-southwest1`)
* **Coste FinOps PRO**: **`$32,00 USD/mes`** (\(\approx \$0,00510\text{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 LTS, Go BFF, Uber H3 Spatial Indexing, OSRM Contraction Hierarchies, VRP Heuristics.

---

## 2. Visión General y Objetivos de Negocio
Optimización de rutas de última milla, ventanas de entrega dinámicas y resolución de problemas de enrutamiento de vehículos (VRP) en tiempo real.
* **KPI Precisión Demanda Envíos (wMAPE)**: **`1,96%`** (\(R^2 = 0,9934\)).
* **KPI Reducción de Kilometraje**: **`-18%`** en rutas urbanas.
* **KPI Latencia P99**: **`8,90 ms`**.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[VRP-101]** Algoritmo de clustering espacial H3 y asignación de flotas de furgonetas eléctricas.
- [x] **[BQML-102]** Modelo de predicción de envíos horarios y tiempos de parada.
- [x] **[ETL-103]** Canal de streaming de eventos de entrega desacoplado de la base transaccional.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-002](file:///home/jaruiz/Desarrollo/docs/adr/adr-002-uber-h3-spatial-indexing.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
