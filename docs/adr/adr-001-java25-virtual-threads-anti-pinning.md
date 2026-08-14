# ADR 001: Adopción de Java 25 Virtual Threads y Prevención Estricta de Carrier Thread Pinning

## Estado
Aprobado (Consilium Romano)

## Contexto
El ecosistema requería gestionar millones de transacciones de E/S concurrentes (APIs REST, gRPC, Firestore, BigQuery, pasarelas de pago) minimizando el consumo de memoria y la sobrecarga de cambio de contexto de los hilos de plataforma tradicionales del SO (1:1).

## Decisión
1. Adoptar **Java 25 (LTS)** con **Virtual Threads (Project Loom)** en todos los servicios de backend y en [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
2. Prohibir de forma estricta el uso de bloques `synchronized` en rutas críticas de E/S, sustituyéndolos por `ReentrantLock` o estructuras concurrentes lock-free (`VarHandle`, `Atomic*`).
3. Prohibir el uso indiscriminado de `ThreadLocal` sin reciclaje para evitar fugas de memoria con millones de hilos virtuales, adoptando `ScopedValue` y `TenantContextHolder` controlado.

## Consecuencias
* **Positivas:** Capacidad para manejar >100.000 solicitudes concurrentes por instancia Cloud Run con un consumo de memoria inferior a 512 MB.
* **Negativas:** Obliga a auditar librerías de terceros para garantizar que no ejecuten `synchronized` alrededor de operaciones bloqueantes.
