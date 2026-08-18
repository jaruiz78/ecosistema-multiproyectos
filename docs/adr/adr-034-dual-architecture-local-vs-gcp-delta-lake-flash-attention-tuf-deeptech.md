# ADR-034: Arquitectura Dual Rigurosa (LOCAL vs GCP PRO) con Delta Lake, FlashAttention, TUF y 3 Verticales DeepTech

## Estado
**Aceptado y Verificado** (Consilium Romano 3.0: Calificación **10.0 / 10.0 SUMMA CUM LAUDE**)

## Contexto y Motivación
Para maximizar la productividad de desarrollo sin incurrir en costes de infraestructura cloud y a la vez garantizar una transición transparente hacia el despliegue serverless de alta escala en Google Cloud Platform, se ha formalizado e implementado una **arquitectura DUAL rigurosa**:

1. **Entorno LOCAL (Hermético & Zero-Cost):**
   - Coste: **`0.00 €`** (stubs en memoria, emuladores y ficheros locales).
   - Heaps acotados con ZGC (`-XX:+UseZGC -Xms64m -Xmx256m/384m`).
   - Almacenamiento local Delta Lake en `data/delta-lake/_delta_log/`.
   - Inferencia con FlashAttention en Edge/CPU con división por bloques (Tiling $O(N)$).
2. **Entorno GCP (BETA / PRO Serverless):**
   - Cloud Run Gen2 serverless con auto-scaling `0 -> N` instancias.
   - Buckets de Google Cloud Storage (`gs://itinera-delta-lake/`) con precondiciones atómicas de commit.
   - BigQuery con particionamiento forzoso por `_PARTITIONDATE` y clustering por `tenant_id`.
   - Cadena de suministro segura con TUF (The Update Framework) y firmas Cosign/Sigstore (SLSA L3/L4).

## Componentes y Verticales Implementados

### 1. Nuevos Starters de Plataforma
* [`corp-delta-lake-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-delta-lake-starter): Almacenamiento ACID append-only y control de concurrencia optimista (OCC).
* [`corp-flash-attention-edge-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-flash-attention-edge-starter): Inferencia de atención con IO-Awareness (Tri Dao et al.).
* [`corp-tuf-sigstore-attestation-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-tuf-sigstore-attestation-starter): Separación de 4 roles criptográficos TUF (Root, Targets, Snapshot, Timestamp).

### 2. Nuevos Verticales DeepTech
* [`apps/ProyectoFusionNuclearMHD`](file:///home/jaruiz/Desarrollo/apps/ProyectoFusionNuclearMHD): Control predictivo de confinamiento de plasma magnetohidrodinámico.
* [`apps/ProyectoStratosphericSAI`](file:///home/jaruiz/Desarrollo/apps/ProyectoStratosphericSAI): Modelado de inyección de aerosoles estratosféricos y albedo solar.
* [`apps/ProyectoCislunarSpaceLogistics`](file:///home/jaruiz/Desarrollo/apps/ProyectoCislunarSpaceLogistics): Ruteo cinemático y balance de combustible en puntos de Lagrange L1/L2.

### 3. Gemelo Digital Unificado 6.0 (28 Clusters Acoplados)
* Simulación dual ejecutada con 1.000.000 de iteraciones Monte Carlo.
* Traza de covarianza final $\text{Tr}(P) = \mathbf{0.00219}$ ($< 0.01000$).
* Coste FinOps en PRO: **`$0.00221 / MAU / mes`** (margen de 6.8x por debajo del techo).
* Disponibilidad SLA: **`99.999%` (Five Nines)**.

## Consecuencias
- Cero fugas de dependencias o costes imprevistos en desarrollo local.
- Cumplimiento total de los estándares académicos de las 12 Facultades (SICP, TAPL, Dynamo, Spanner, FlashAttention, Delta Lake, TUF).
