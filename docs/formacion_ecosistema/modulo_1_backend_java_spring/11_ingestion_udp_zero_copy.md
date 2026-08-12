# Ingestión de Red de Baja Latencia: Sockets UDP, Zero-Copy y Direct Buffers

## 1. El Problema de la Ingestión Masiva en REST
En ecosistemas altamente interconectados como el **Unified Digital Twin**, el orquestador físico (escrito en Python) genera telemetría de estado estocástico de alta frecuencia (decenas de veces por segundo). Si los microservicios de resiliencia en Java consumieran estos datos mediante llamadas HTTP REST sobre TCP (o incluso gRPC clásico):
- **Impacto en el GC (Garbage Collector):** La serialización/deserialización JSON constante satura el Heap, provocando picos de *Young GC* y eventuales *Stop-The-World*.
- **Overhead de TCP:** El *3-way handshake* y los *ACKs* de TCP añaden latencia innecesaria en flujos donde la pérdida esporádica de un datagrama es tolerable (la covarianza se actualiza inmediatamente en el siguiente tick).

## 2. La Solución: Streaming UDP y Zero-Copy
Para la telemetría predictiva unidireccional (Python -> Java), se abandona el estándar de capa de aplicación pesada en favor de sockets asíncronos y **UDP (User Datagram Protocol)**.

### A. Ventajas Técnicas
- **Latencia mínima:** Sin control de flujo ni estado de conexión.
- **Buffers Directos (DirectByteBuffer):** En Java (mediante `java.nio.channels.DatagramChannel`), los bytes se leen directamente en memoria *off-heap*. El sistema operativo realiza un volcado de red a la RAM (Zero-Copy) sin que el Garbage Collector de Java intervenga en la asignación de memoria.

### B. Implementación del Predictive Circuit Breaker
El componente `PredictiveCircuitBreaker` inicializa un canal UDP en el puerto `50052`. Este canal lee de forma no bloqueante (Non-Blocking IO). Sólo cuando el payload se decodifica en un umbral de peligro, se despacha una instrucción al hilo principal, garantizando que el coste de absorción de telemetría regular es prácticamente cero $O(1)$.

## 3. Conclusión Arquitectónica
Adoptar protocolos *connectionless* y deserializaciones minimalistas off-heap es el estándar imperativo para la fusión entre Modelos Físicos Continuos y Arquitecturas Reactivas Empresariales.
