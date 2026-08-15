# Módulo 5 - Lección 2: BigQuery SQL, Property Graphs (GQL) & Optimización FinOps

---

## 1. 🐣 Ancla Mental Feynman & Analogía Isomórfica

### El Modelo Intuitivo: BigQuery SQL, Property Graphs (GQL) & Optimización FinOps
Para comprender **BigQuery SQL, Property Graphs (GQL) & Optimización FinOps** sin caer en la trampa de la jerga técnica, debemos anclar el concepto en un problema físico observable:
* Todo sistema en computación resuelve un dilema fundamental: cómo organizar recursos limitados (tiempo de cálculo, espacio de memoria, ancho de banda o energía) para que el trabajo se realice sin bloqueos ni errores.
* En **BigQuery SQL, Property Graphs (GQL) & Optimización FinOps**, la clave reside en eliminar pasos redundantes y asegurar que cada componente conozca únicamente la información mínima indispensable para cumplir su función, tal como una línea de montaje bien coordinada donde nadie tiene que adivinar qué hizo el compañero anterior.

---


## 1. FinOps y Control de Costes por Consulta (< `$0.015 USD`/MAU)

BigQuery cobra por terabytes de datos escaneados. Ejecutar consultas no optimizadas (`SELECT *`) sobre tablas con miles de millones de registros puede agotar el presupuesto de infraestructura.

```mermaid
graph TD
    subgraph Flujo de Consulta Optimizado (bq-dry-run-optimizer)
        Q_IN[SQL Query / GQL Query] --> DRY[Dry-Run Validation]
        DRY --> COST_CHECK{"¿Bytes escaneados < Limite (100MB)?"}
        
        COST_CHECK -->|Sí| EXEC[Ejecutar Query en BigQuery]
        COST_CHECK -->|No| REJECT[Rechazar Query & Exigir Filtro por Partición/Clúster]
    end
```

### Reglas de Optimización en BigQuery
1. **Nunca usar `SELECT *`**: Seleccionar únicamente las columnas estrictamente necesarias.
2. **Particionamiento por Fecha/Hora (`PARTITION BY DATE(timestamp)`)**: Reduce los MB escaneados al rango temporal especificado en el `WHERE`.
3. **Clusterización por Inquilino (`CLUSTER BY tenant_id, user_id`)**: Agrupa los datos físicamente en disco para lecturas aceleradas.

---

## 2. Ejemplo SQL Optimizado con Particionamiento y Clustering

```sql
-- Creación de Tabla Optimizada FinOps
CREATE TABLE `saas-regantes.analytics.telemetry_events`
(
    tenant_id STRING,
    sensor_id STRING,
    flow_rate FLOAT64,
    event_timestamp TIMESTAMP
)
PARTITION BY DATE(event_timestamp)
CLUSTER BY tenant_id, sensor_id;

-- Consulta de Alta Eficiencia (Escaneo Quirúrgico)
SELECT
    sensor_id,
    AVG(flow_rate) AS avg_flow
FROM `saas-regantes.analytics.telemetry_events`
WHERE DATE(event_timestamp) BETWEEN '2026-08-01' AND '2026-08-10'
  AND tenant_id = 'tenant-valencia-01'
GROUP BY sensor_id;
```

---

## 3. Consultas en Grafos de Propiedades con GQL (Graph Query Language)

En BigQuery se pueden definir y consultar grafos de topología hídrica o redes de movilidad utilizando sintaxis **GQL (ISO Graph Query Language)**.

```sql
-- Consulta GQL para encontrar rutas de distribución hídrica activas entre embalses y parcelas
GRAPH `saas-regantes.topology.water_network`
MATCH (reservoir:Infrastructure {type: 'RESERVOIR'})
      -[pipe:PIPE_LINE {status: 'ACTIVE'}]-> (plot:Plot)
WHERE reservoir.tenant_id = 'tenant-valencia-01'
RETURN reservoir.name AS origen, pipe.capacity_lps AS capacidad, plot.id AS parcela_destino;
```


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **BigQuery SQL, Property Graphs (GQL) & Optimización FinOps** a un estudiante de secundaria, **sin usar las palabras:** "BigQuery", "SQL,", "Property" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
