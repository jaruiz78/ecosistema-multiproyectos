# ADR-014: Estandarización de Errores RFC 9457, Resiliencia Adaptativa y Bucle de Auto-Remediación

## Estado
**Aceptado** (Consilium Romano Architecture Review)

## Contexto
1. En el ecosistema multi-proyecto conviven servicios en Java 25 (Spring Boot 4.x), Go 1.24 (BFFs, Proxies y Workers) y clientes en Flutter y React.
2. Anteriormente existían discrepancias en las respuestas de error: respuestas en texto plano (`http.Error`), mapas JSON ad-hoc (`Map<String, Object>`), respuestas vacías con solo código HTTP (`.build()`), y falta de correlación del identificador de traza (`traceId`) devuelto al cliente.
3. La resiliencia ante fallos dependía en algunos puntos de variables en memoria aisladas (`AtomicBoolean` en controladores), impidiendo la coordinación ante escalado horizontal en Cloud Run y exponiendo al sistema a sobrecargas en cascada sin cabeceras semánticas (`Retry-After`).
4. Para operar con alta confiabilidad y bajo coste por usuario (`< 0.015 USD/MAU/mes`), es necesario un mecanismo de auto-remediación rápida, clasificación de incidentes por código de dominio y aprendizaje continuo frente a derivas de servicio.

## Decisión

### 1. Estandarización Universal bajo IETF RFC 9457 / RFC 7807 (`application/problem+json`)
- **Tipo MIME Obligatorio**: Todas las respuestas con código HTTP `4xx` y `5xx` deben emitirse con cabecera `Content-Type: application/problem+json`.
- **Esquema Canónico**:
  - `type`: URI unívoca del catálogo de error (`https://api.corp.local/errors/{code}`).
  - `title`: Resumen corto y legible del tipo de problema.
  - `status`: Código de estado HTTP entero.
  - `detail`: Explicación humana contextualizada del fallo.
  - `instance`: URI del endpoint específico donde se produjo la anomalía.
  - `traceId`: Identificador W3C unívoco extraído de `traceparent` o MDC.
  - `errorCode`: Código enumerado corporativo (`CorpErrorCode`, e.g., `CORP_VALIDATION_ERROR`, `CORP_LOAD_SHEDDING_ACTIVE`).
  - `timestamp`: Marca de tiempo ISO-8601 UTC.
  - `invalidParams`: Array estructurado `[{"name": "...", "reason": "..."}]` ante fallos de validación (`400 Bad Request`).

### 2. Chasis Transversal (`corp-core-spring-boot-starter` & Go `WriteProblem`)
- **`CorpGlobalExceptionHandler`**: Manejador `@RestControllerAdvice` base en Java 25 que intercepta automáticamente validaciones, reglas de dominio, estados inconsistentes, violaciones de seguridad y excepciones inesperadas.
- **`WriteProblem` en Go**: Helper en `bff-go` y `fraud-shield-api` que asegura la misma estructura y tipo MIME en todas las pasarelas Go.
- **`IdempotencyFilter`**: Deduplicación transversal para operaciones mutables (`POST`/`PUT`) mediante cabecera `X-Idempotency-Key` y caché con expiración atómica.

### 3. Resiliencia en Cuatro Niveles y Bucle de Auto-Remediación
```
[Petición Cliente] ──> [Adaptive Bulkhead (429 + Retry-After)] ──> [Predictive Circuit Breaker (EnKF)]
                             │                                              │
                       (CPU > 85%)                                   (Covarianza > 0.5)
                             ▼                                              ▼
               [Load Shedding Inmediato]                      [Fast-Fallback L1 Cache / Stub]
                             │                                              │
                             └───────────────┬──────────────────────────────┘
                                             ▼
                        [Métricas Micrometer + OTEL Spans]
                                             ▼
                       [Ingesta Telemetría / BigQuery & SQLite]
                                             ▼
                      [Detección de Deriva ADWIN / Auto-Ajuste]
```

1. **Nivel 1: Protección de Carga (Load Shedding)**:
   - `AdaptiveBulkheadFilter` descarta tráfico no prioritario con `429 Too Many Requests` y cabecera `Retry-After: 5` cuando el CPU de la instancia supera el 85%.
2. **Nivel 2: Circuito Predictivo (EnKF Covariance Guard)**:
   - `PredictiveCircuitBreaker` interrumpe preventivamente llamadas a componentes inestables cuando la covarianza estocástica del Gemelo Digital supera 0.5, activando fallbacks deterministas sin bloquear los Virtual Threads.
3. **Nivel 3: Aislamiento Asíncrono (Dead Letter Governance)**:
   - Los fallos no recuperables en streaming se derivan a colas DLQ (`*-dlq`) con metadatos de error y trazabilidad W3C completa.
4. **Nivel 4: Aprendizaje y Post-Mortem Automatizado**:
   - Registro de telemetría estructurada en `simulations_telemetry.db` y BigQuery para análisis de patrones de error, detección de deriva de APIs externas (ADWIN) y ajuste dinámico de umbrales.

### 4. Normalización RESTful (Nivel 2/3 Richardson Maturity Model)
- Rutas estandarizadas con sustantivos en plural (`/api/v1/padron/parcelas`, `/api/v1/padron/comuneros`, `/api/v1/portal-regante/turnos`, `/api/v1/itineraries`).
- Respuestas `201 Created` acompañadas obligatoriamente de cabecera `Location: {uri_del_recurso}`.
- Respuestas `204 No Content` en eliminaciones exitosas.
- Eliminación de antipatrones RPC en URIs y de bloques `catch (Exception e)` locales que devuelvan respuestas vacías o strings planos.

## Consecuencias
- **Experiencia de Integración**: Los clientes (Flutter, React, integradores externos) procesan los errores de forma determinista y tipada sin riesgo de parsing exceptions.
- **Trazabilidad Instantánea**: El campo `traceId` en el cuerpo del error permite a soporte y a los ingenieros localizar el log y span exacto en Cloud Trace en cuestión de segundos.
- **Estabilidad de Plataforma**: Cero caídas en cascada gracias a la combinación de Bulkhead adaptativo, Circuit Breaker predictivo y Store-and-Forward asíncrono.
- **Zero-PII Compliance**: Todos los logs y respuestas están saneados y libres de datos personales o credenciales.

## Referencias
- IETF RFC 9457: Problem Details for HTTP APIs (reemplaza a RFC 7807)
- Richardson Maturity Model (Martin Fowler, 2010)
- ADR-001: file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
- ADR-012: file:///home/jaruiz/Desarrollo/docs/adr/adr-012-universal-w3c-logging-and-zero-pii.md
- ADR-013: file:///home/jaruiz/Desarrollo/docs/adr/adr-013-pubsub-resilience-batching-and-dlq-governance.md
