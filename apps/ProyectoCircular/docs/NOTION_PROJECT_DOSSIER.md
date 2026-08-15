# Notion Project Dossier: ProyectoCircular (Economía Circular y Trazabilidad Bio-Residuos)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: 
* **Líder Técnico / Arquitecto**:  & 
* **Estado en Kanban**: 
* **Prioridad**: 
* **Región de Despliegue**: GCP Cloud Run () / Edge Local
* **Coste FinOps PRO**: **** (\(pprox $0,0045	ext{ USD/MAU/mes}\))
* **Stack Tecnológico**: Java 25 / Spring Boot 4.0 / Loom

---

## 2. Visión General y Objetivos de Negocio
Valorización energética y balances de masa orgánicos.
* **KPI Rendimiento**: Latencia P99 sub-15ms.
* **KPI Disponibilidad SLA**: ****.
* **Eficiencia Asintótica**: \(\mathcal{O}(1)\) / \(\mathcal{O}(N \log N)\).

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[PROY-101]** Especificación de arquitectura hexagonal y modelo de dominio puro con Records Java 25.
- [x] **[PROY-201]** Integración de pipeline de streaming ETL y telemetría analítica hacia BigQuery.
- [x] **[PROY-301]** Multi-stage Dockerfile distroless optimizado para cold-start en Cloud Run.
- [x] **[PROY-401]** Suite de pruebas unitarias y de integración Zero-Mockito.

### B. Tareas en Curso (In Progress)
- [ ] **[PROY-501]** Optimización AOT Leyden CDS pre-compilada en CI/CD.

### C. Backlog Priorizado
- [ ] **[PROY-601]** Aceleración por hardware mediante Java Vector API y bindings Panama.

---

## 4. Arquitectura y Puertos Hexagonales
* **Inbound Ports**: Adaptadores de entrada REST/gRPC y listeners de eventos de streaming.
* **Outbound Ports**: Repositorios inmutables con RLS y sinks de eventos analíticos BigQuery.

---

## 5. Recursos e Infraestructura Asociada
* **Servicio Cloud Run**:  (1 vCPU, 512MiB RAM, min=0, max=10).
* **BigQuery Sink**: Datasets analíticos particionados por fecha y clusterizados por tenant.
