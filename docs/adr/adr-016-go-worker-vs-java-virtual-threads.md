# ADR 016: Elección de Go para Workers de Ingesta (BFF) vs Java Virtual Threads

## 1. Contexto

En el ecosistema PCT, manejamos ingestas masivas de telemetría GPS provenientes de TaxiCaller. Se requería una solución capaz de soportar picos de 10,000 vehículos moviéndose simultáneamente (miles de peticiones por segundo) y deduplicarlas espacialmente (H3) antes de inyectarlas en el pipeline analítico ETL de DuckDB/BigQuery.
Teníamos dos opciones principales en el ecosistema corporativo:
1. Re-utilizar el backend corporativo Java 25 (con Spring Boot 4.1 y Loom Virtual Threads).
2. Desarrollar un BFF en Go.

## 2. Decisión

Se decide implementar el **BFF (Backend for Frontend) y los workers de ingesta en Go**, específicamente utilizando goroutines directas y buffers no bloqueantes de canales, y relegar a Java 25 las tareas transaccionales, de dominio puro, y de integración compleja de servicios.

## 3. Justificación y Análisis Asintótico $O(1)$

1. **Gestión de Memoria Predictiva y GC Pauses**: 
   A pesar de las mejoras de Generational ZGC en Java 25, la generación intensiva de miles de objetos de telemetría efímeros provoca picos de latencia de GC. En Go, la estructura por valor (`structs`) y la capacidad de anclar datos contiguos (escape analysis a stack) permite ingestas O(1) con casi nula recolección de basura.
2. **Buffer No Bloqueante (Backpressure)**: 
   Go permite canales con semántica de contrapresión ultra-eficiente (`select { case ch <- evt: ... default: drop }`).
3. **Costo Computacional e Inicialización (Leyden vs Binario Estático)**: 
   Go compila en un binario estático que arranca en <10ms sin necesidad de pre-entrenamiento (AOT/CDS Leyden).
4. **Sinergia Arquitectónica (Grounded Architecture)**: 
   Como dictan los patrones de LMAX y sistemas Edge, separar la ingesta masiva (Go) de la lógica transaccional de dominio (Java) previene que la aplicación Core sufra *Carrier Thread Pinning* si hay colapsos o picos de IO no previstos.

## 4. Consecuencias

- **Positivas**: Reducción drástica del tamaño de RAM requerida por MAU en el cluster de ingesta. Costo < 0.015 USD garantizado. Protección del backend transaccional de Java ante ataques de DDoS.
- **Negativas**: Mantenimiento dual de base de código (Java y Go). Se requiere el uso del framework `testcontainers-go` y el `go-duckdb` de forma aislada.

## 5. Implementación Referencia

Se ha materializado en `pctMultiMicroservices/services/bff-go/etl_telemetry_worker.go` y `duckdb_exporter.go` aplicando deduplicación H3 in-memory de forma concurrente mediante bloqueos `sync.RWMutex` optimizados.
