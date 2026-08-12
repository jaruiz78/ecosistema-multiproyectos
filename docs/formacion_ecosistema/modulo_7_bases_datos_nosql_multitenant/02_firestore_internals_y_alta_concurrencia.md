# Firestore Internals y Patrones de Alta Concurrencia

Firestore es una base de datos NoSQL documental, Serverless y optimizada para escalabilidad masiva y sincronización en tiempo real. A diferencia de las bases de datos relacionales tradicionales, la arquitectura de Firestore exige un cambio de paradigma (de la normalización a la desnormalización) y un conocimiento profundo de sus límites físicos para evitar cuellos de botella (Hotspots).

Este documento expone la anatomía de Firestore, sus restricciones de escritura y las técnicas avanzadas requeridas en ecosistemas B2B2C como *AppViajes* y *SaaSRegantes*.

---

## 1. Arquitectura Base y Límites Físicos (Hard Limits)

El almacenamiento subyacente de Firestore está fuertemente tipado e indexado por defecto. Sin embargo, su infraestructura global impone límites estrictos derivados de los algoritmos de consenso distribuido (variantes de Paxos/Spanner en el backend de Google).

*   **1 Escritura por Segundo por Documento:** El límite más crítico. No puedes actualizar un solo documento (ej. un contador global de "viajes_completados") más de 1 vez por segundo de forma sostenida sin incurrir en latencia elevada o errores de contención.
*   **500 Escrituras por Segundo por Colección:** En condiciones iniciales (sin pre-calentamiento), una colección nueva o con un patrón de clave secuencial puede colapsar si excede las 500 escrituras por segundo en un espacio de claves contiguo (*Lexicographical Hotspotting*).
*   **Costes por Operación:** En Firestore pagas por:
    1. Documentos Leídos (Lectura).
    2. Documentos Escritos (Escritura).
    3. Documentos Eliminados.
    4. Almacenamiento y ancho de banda.
    **Conclusión:** Realizar una consulta que devuelve 10,000 documentos para calcular un `SUM()` costará 10,000 operaciones de lectura. En su lugar, el valor debe pre-agregarse (Cloud Functions) o volcarse a BigQuery (OLAP).

---

## 2. Índice Compuesto y *Zig-Zag Merge Join*

Para consultas que filtran y ordenan simultáneamente, Firestore utiliza un motor de indexación avanzado.

### 2.1 Limitaciones del Indexado Automático
Por defecto, Firestore indexa cada campo individualmente de forma ascendente y descendente (Índices Simples).
Si realizas una consulta con dos filtros de igualdad: `WHERE tenant_id == 'T1' AND status == 'ACTIVE'`, Firestore utiliza un algoritmo llamado **Zig-Zag Merge Join**. El motor intercala y cruza los punteros de los dos índices individuales para encontrar la intersección sin requerir un índice especial.

### 2.2 Requisitos de Índice Compuesto (Composite Index)
El Zig-Zag Merge Join falla si la consulta combina filtros de rango (`<`, `>`, `<=`, `>=`) con desigualdades u ordenamientos múltiples.
Ejemplo: `WHERE tenant_id == 'T1' AND timestamp > '2026-01-01' ORDER BY timestamp DESC`.
En este caso, la arquitectura exige la creación explícita de un **Índice Compuesto** (`tenant_id` ASC, `timestamp` DESC). En *SaaSRegantes*, la creación de índices compuestos multi-tenant es crítica para evitar fallos en producción (ya que Firestore lanzará una excepción `FAILED_PRECONDITION` y no devolverá resultados).

---

## 3. Estrategias de Desnormalización y Anti-Hotspotting

El diseño en Firestore implica estructurar los datos basándose en **cómo serán leídos por el cliente**, y no en cómo se relacionan lógicamente las entidades.

### 3.1 Contadores Distribuidos (Distributed Counters)
Para superar el límite de 1 escritura/segundo en agregaciones (ej. Total de riegos activos), se emplea el patrón de **Sharding de Contadores**:
1. En lugar de tener un documento `{ total: 50 }`, se crea una subcolección `shards` con $N$ documentos (ej. 10 shards).
2. Cuando se incrementa el contador, el cliente/backend elige un $shard$ aleatorio (del 0 al 9) y lo incrementa.
3. Para leer el total, se leen los 10 shards y se suman.
4. Esto eleva el límite a $N$ escrituras por segundo, mitigando la contención.

### 3.2 Duplicación Inversa y Fan-Out Estático
Si en *AppViajes* el perfil de un conductor (nombre, foto, placa) se muestra junto a un viaje, una DB relacional haría un `JOIN(Viajes, Conductores)`. Firestore no soporta JOINs.
*   **Solución (Data Duplication):** Copiar los metadatos inmutables o de baja rotación del conductor (nombre, placa) directamente en el documento del viaje al momento de crearlo.
*   **Mantenimiento (Fan-Out):** Si el conductor cambia su foto, una Cloud Function escuchará el evento (Eventarc/Triggers) y actualizará (Fan-Out) en cascada los viajes donde ese conductor participe. El coste de escritura esporádico se compensa infinitamente con lecturas $O(1)$ sin JOINs.

### 3.3 Claves Primarias Aleatorias y GUIDs
Nunca debes usar identificadores secuenciales (ej. `Viaje-001`, `Viaje-002`) o *timestamps* monótonos crecientes como IDs (nombres de documentos). Esto provoca que todas las nuevas escrituras se dirijan al mismo servidor físico en el clúster de Google, causando un **Lexicographical Hotspot** masivo que estrangulará el throughput a 500 ops/seg.
Se deben utilizar identificadores UUID v4 o el algoritmo criptográfico nativo de Firestore (`collection.doc().id`), los cuales garantizan una distribución aleatoria en el espacio de claves y un escalado infinito.

---

## 4. Reflexión del Consilium
Firestore castiga severamente el pensamiento relacional. Su arquitectura favorece la lectura inmediata $\mathcal{O}(1)$ a expensas de la redundancia de datos (desnormalización controlada) y del procesamiento asíncrono (Triggers). No es la herramienta para analítica; su rol es ser la capa operacional en tiempo real y el puente transaccional hacia BigQuery.
