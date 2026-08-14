# Notion Project Dossier: core-geogrid-h3 (Indexación Espacial Hexagonal H3)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `core-geogrid-h3-engine`
* **Líder Técnico / Arquitecto**: `@Mobile-Mobility-Architect`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región / Despliegue**: Librería Embebida en Memoria (Go / Java / Python)
* **Coste FinOps PRO**: **`$0,00 USD/mes`**
* **Stack Tecnológico**: Uber H3 C/Go Bindings, Java Native Panama FFM, Python H3.

---

## 2. Visión General y Objetivos de Negocio
Indexación espacial universal basada en mallas hexagonales jerárquicas Uber H3 (Resoluciones 7, 8, 9 y 10) para movilidad urbana, sectores agrícolas, logística y asignación territorial.
* **KPI Tiempo de Indexación**: **`< 120 ns`** por coordenada (lat/lon -> H3Index).
* **KPI Reducción de Ruido**: Deduplicación del 65% de eventos espaciales redundantes.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[H3-101]** Mapeo de resoluciones jerárquicas (Res 8 para dispatching, Res 9 para paradas exactas, Res 10 para agro-parcelas).
- [x] **[H3-102]** Algoritmos de vecindad \(k\)-ring y compactación/descompactación de conjuntos espaciales.
- [x] **[H3-103]** Integración nativa con esquemas particionados y clusterizados de BigQuery.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-002](file:///home/jaruiz/Desarrollo/docs/adr/adr-002-uber-h3-spatial-indexing.md), [ADR-009](file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md).
