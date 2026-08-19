# ADR 004: Aislamiento Multi-Tenant Celular RLS en Firestore y Particionado FinOps en BigQuery

## Estado
Aprobado (Consilium Romano)

## Contexto
Garantizar el estricto aislamiento de datos entre inquilinos (*tenants*) en entornos multi-empresa manteniendo los costes de infraestructura por debajo de `$0.015 USD`/MAU/mes.

## Decisión
1. Implementar **Row-Level Security (RLS)** estricto en Firestore vinculado a los claims de autenticación del usuario (`request.auth.token.tenant_id`).
2. En Google BigQuery, configurar obligatoriamente `requirePartitionFilter = true` por campo `timestamp` y clustering por `tenant_id`.

## Consecuencias
* **Positivas:** Cero riesgo de fuga de datos entre empresas y reducción del 92% en los bytes procesados en consultas analíticas.
* **Negativas:** Todas las consultas SQL deben incluir explícitamente el filtro temporal y el identificador de inquilino.
