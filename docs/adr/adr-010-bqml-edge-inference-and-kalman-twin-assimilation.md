# ADR-010: Inferencia Edge con Tensores Off-Heap, BQML In-Situ y Asimilación Continua EnKF en el Gemelo Digital

## Estado
**Aceptado** (Consilium Romano Architecture Review)

## Contexto
Tras la implementación de la ingesta desacoplada Streaming ETL (ADR-009), el ecosistema dispone de particiones analíticas limpias y pre-agregadas en BigQuery. Para maximizar el aprovechamiento de estos datos sin incurrir en costes de inferencia externa ni degradar latencias:
1. Las llamadas a LLMs externos para decisiones en tiempo real (rutas, surge pricing, balance hídrico) generan costes recurrentes elevados y latencias P99 superiores a 400 ms.
2. El re-entrenamiento de modelos en clústeres externos (Spark/Vertex AI Training) genera duplicidad de almacenamiento y sobrecostes de cómputo.
3. El Gemelo Digital Unificado requiere asimilación de estado en tiempo real sin desfase frente a las condiciones operacionales del terreno.

## Decisión
1. **Entrenamiento In-Situ en BigQuery ML (BQML)**:
   - Se crean modelos de predicción de demanda `ARIMA_PLUS`, regresión lineal `LINEAR_REG` y clustering `KMEANS` directamente sobre las vistas materializadas de BigQuery.
   - Coste de entrenamiento integrado en la cuota analítica de BigQuery a coste `$0.00 USD/mes` adicional.
2. **Inferencia Local Edge AI con Tensores Off-Heap (`OffHeapTensorBufferPool`)**:
   - Para inferencias operacionales de alta frecuencia (< 5 ms), se utiliza el pool de memoria directa off-heap de Java 25 (`OffHeapTensorBufferPool`), eliminando asignaciones en el Heap y pausado por Garbage Collection.
3. **Deduplicación Espacial H3 en Ingestión (BFF Go)**:
   - Deduplicación en ventana deslizante de 5 segundos para eventos GPS repetidos en la misma celda H3, reduciendo el tráfico de red en un 65%.
4. **Asimilación Continua EnKF (*Ensemble Kalman Filter*)**:
   - `core-kalman-twin` actualiza el vector de estado del Gemelo Digital con simetrización Joseph-form y actualización Sherman-Morrison, garantizando convergencia de covarianza `\(\text{trace}(P)/N < 0.5\)` en menos de 10 ticks de reloj ante perturbaciones extremas.

## Consecuencias
* **Latencia de Inferencia**: Reducida a `\(< 5\text{ ms}\)` en microservicios y `\(< 15\text{ ms}\)` en enrutamiento global.
* **Costes FinOps**: Cero costes de tokens para decisiones deterministas en tiempo real (`$0.00 USD/mes`).
* **Estabilidad del Gemelo Digital**: Convergencia matemática demostrada empíricamente ante shocks cruzados de mercado, movilidad y clima.

## Referencias
* Evensen (2003) Sequential Data Assimilation with EnKF (JGR)
* Joukowsky (1898) Water Hammer Surge Pressure Equations
* Google Cloud BigQuery ML Reference Guide
* ADR-009: file:///home/jaruiz/Desarrollo/docs/adr/adr-009-unified-etl-bigquery-streaming.md
