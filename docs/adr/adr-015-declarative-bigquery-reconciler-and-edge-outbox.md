# ADR-015: Reconciliador Declarativo de BigQuery, Degradación Elegante en Edge y Contratos Canónicos

## Estado
Aceptado / Implementado (2026-08-14)

## Contexto
Durante la auditoría integral de la arquitectura del ecosistema (2026–2031), se identificaron cuatro necesidades clave para optimizar la gobernanza FinOps, la resiliencia en dispositivos desconectados y la integración continua:
1. **Gobernanza FinOps en BigQuery**: Prevenir consultas accidentales de tabla completa mediante la imposición automática de `require_partition_filter = true` y autocorrección de schema drift en el arranque.
2. **Resiliencia en Edge (>72h sin red)**: Evitar la saturación del almacenamiento flash en terminales IoT / móviles (AppViajes y SaaSRegantes) mediante poda y compactación inteligente.
3. **Contratos Canónicos Unificados**: Centralizar esquemas Protobuf y Apache Avro en `corp-contracts-starter` con validación multi-lenguaje.
4. **Benchmarks Continuos Nocturnos**: Automatizar la detección de regresiones de latencia y derivas estocásticas en la ganancia de Kalman (\(P > 0.50\)).

## Decisiones

### 1. Reconciliador Declarativo en BigQuery (`DeclarativeBigQuerySchemaReconciler`)
- En el arranque de cada vertical analítico, se compara la especificación formal (`TableSpecification`) frente a los metadatos reales de BigQuery.
- Se ejecutan automáticamente sentencias DDL para:
  - `CREATE TABLE IF NOT EXISTS` con particionado por fecha y clustering multi-tenant.
  - `ALTER TABLE SET OPTIONS (require_partition_filter = true)`.
  - `ALTER TABLE ADD COLUMN` ante schema drift.

### 2. Compactador y Degradación Elegante en Edge (`EdgeOutboxCompactor`)
- Clasificación de eventos en cuatro prioridades: `CRITICAL` (inmutable), `HIGH`, `NORMAL`, `LOW`.
- Tres niveles de degradación según duración offline y uso de almacenamiento:
  - `OPTIMAL` (<24h): Poda de eventos procesados.
  - `DEGRADED` (24h–72h): Downsampling de eventos `LOW` (último ping por agregado).
  - `CRITICAL_OFFLINE` (>72h): Downsampling de eventos `NORMAL` (última lectura por sensor), preservación al 100% de `CRITICAL`/`HIGH`, y ejecución de `PRAGMA vacuum`.

### 3. Centralización de Contratos (`corp-contracts-starter`)
- Ubicación de esquemas en `src/main/resources/proto/` y `src/main/resources/avro/`.
- Pipeline de validación sintáctica y compatibilidad Java 25 / Go 1.24 vía `scripts/generate_unified_contracts_artifacts.sh`.

### 4. Pipeline Nocturno de Benchmarks (`cloudbuild_nightly_benchmarks.yaml`)
- Ejecución diaria con disparador Cloud Scheduler / Cloud Build.
- Detección de regresiones frente a límites: \(p50 \le 2.0\text{ ms}\), \(p95 \le 5.0\text{ ms}\), \(P_{\text{EnKF}} \le 0.500\), Coste \(\le \$0.0150\text{ USD/MAU/mes}\).

## Consecuencias
- **FinOps**: Eliminación total del riesgo de scans no particionados en BigQuery.
- **Resiliencia**: Los terminales de campo y vehículos pueden operar más de 7 días sin conexión sin saturar almacenamiento ni perder eventos críticos.
- **Rendimiento**: Tiempos de reconciliación \(< 2\text{ ms}\) y suite de pruebas ejecutada en \(< 3\text{ segundos}\).
