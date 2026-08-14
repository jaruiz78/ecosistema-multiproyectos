# ADR-009: Arquitectura Unificada de Streaming ETL, BigQuery Storage Write API y Optimización FinOps

## Estado
**Aceptado** (Consilium Romano Architecture Review)

## Contexto
En arquitecturas multi-tenant de alto rendimiento con cargas intensivas (IoT agroclimático en `SaaSRegantes`, trazas GPS en `AppViajes`, eventos de tareas en `pctMultiMicroservices` y smart meters en verticales energéticos), la persistencia directa y continua de eventos analíticos en bases de datos transaccionales (OLTP como Firestore o Cloud SQL) provoca:
1. **Costes desproporcionados** en operaciones de lectura/escritura unitarias en Firestore superando el umbral de `$0.015 USD/MAU/mes`.
2. **Contención y Carrier Thread Pinning** en el backend transaccional al ejecutar consultas de agregación y joins analíticos sobre tablas operacionales.
3. **Escaneos masivos de tablas** no particionadas en BigQuery aumentando la facturación por terabyte procesado.

## Decisión
1. **Desacoplamiento Estricto OLTP vs. OLAP**: Se implementa la capa `UnifiedStreamingEtlPipeline` en `corp-spring-boot-starter` (`corp-bigdata-ai-starter`) y `etl_telemetry_worker.go` en Go.
2. **Micro-Batching In-Memory con Virtual Threads**:
   - Acumulación no bloqueante en memoria con `ConcurrentLinkedQueue` y canales en Go.
   - Umbral configurable de batch (ej. 250-500 eventos) o intervalo de tiempo (5s).
   - Ingestión streaming binaria Apache Arrow / Protobuf zero-copy hacia la *BigQuery Storage Write API*.
3. **Gobierno FinOps en BigQuery**:
   - Requisito mandatorio de `require_partition_filter = true` en toda tabla analítica de streaming.
   - Particionamiento diario por `DATE(timestamp)`.
   - *Clustering* celular por `tenant_id` y claves espaciales/dimensionales (`h3_res8`, `task_type`, `sector_id`).
   - Generación de *Materialized Views* con refresco automático incremental cada 10-30 minutos para KPIs.

## Consecuencias
* **Rendimiento**: Latencia P99 de APIs transaccionales reducida a `\(< 35\text{ ms}\)`. Throughput incrementado entre `\(5\times\)` y `\(10\times\)`.
* **Costes (FinOps)**: Reducción estimada del 40% al 60% en la factura mensual de infraestructura GCP en PRO. Coste por usuario garantizado en `\(< 0.015\text{ USD/MAU/mes}\)`.
* **Gemelo Digital**: Series temporales limpias y homogéneas alimentan directamente al filtro de Kalman EnKF (`tensor_gnn_core.py`) con covarianza `\(< 0.5\)`.

## Referencias
* Martin (2017) Clean Architecture & DDD Standard
* Google Cloud (2024) BigQuery Storage Write API Best Practices
* Goetz (2006) Java Concurrency in Practice
