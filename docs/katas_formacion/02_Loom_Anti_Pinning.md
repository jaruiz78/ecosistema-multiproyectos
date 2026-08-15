# Kata 02: Loom Anti-Pinning (Java 25)

## Objetivo
Evitar el Carrier Thread Pinning en el uso de Virtual Threads con Spring Boot 4.0.

## Reglas
- **Prohibido:** `synchronized` en bloques de I/O o locks prolongados.
- **Permitido:** `ReentrantLock` o `ScopedValue`.

## Ejercicio
Convierte este bloque problemático:
```java
public synchronized void processPayment() {
    Thread.sleep(100);
}
```
En código válido:
```java
private final ReentrantLock lock = new ReentrantLock();
public void processPayment() {
    lock.lock();
    try {
        Thread.sleep(100);
    } finally {
        lock.unlock();
    }
}
```
