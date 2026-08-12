# Módulo 1 - Lección 1: Concurrencia Masiva con Java 25 & Virtual Threads (Loom)

## 1. Mapeo de Hilos Tradicionales vs Virtual Threads

En Java tradicional, cada `Thread` de Java se mapeaba 1:1 a un hilo del sistema operativo (Platform Thread), lo que consumía ~1MB de pila por hilo. En Java 25, los **Virtual Threads** son gestionados directamente por la JVM en espacio de usuario.

```mermaid
graph TD
    subgraph Espacio de Usuario / JVM Java 25
        VT1[Virtual Thread 1]
        VT2[Virtual Thread 2]
        VT3[Virtual Thread 3]
        VT4[Virtual Thread 4]
        VT5[Virtual Thread 5]
        VT6[Virtual Thread 6]
    end

    subgraph Carrier Threads / Hilos de Plataforma (JVM-to-OS)
        CT1[Carrier Thread A]
        CT2[Carrier Thread B]
    end

    subgraph Núcleos de CPU del Sistema Operativo
        CPU1[Core CPU 1]
        CPU2[Core CPU 2]
    end

    VT1 -->|Montado temporalmente| CT1
    VT2 -->|Montado temporalmente| CT1
    VT3 -->|Montado temporalmente| CT1
    VT4 -->|Montado temporalmente| CT2
    VT5 -->|Montado temporalmente| CT2
    VT6 -->|Montado temporalmente| CT2

    CT1 --> CPU1
    CT2 --> CPU2
```

---

## 2. Prevención de Carrier Thread Pinning

El *Carrier Thread Pinning* ocurre cuando un Virtual Thread realiza una operación bloqueante dentro de un bloque o método `synchronized`. Esto "engancha" el hilo de plataforma e impide que la JVM desmonte el Virtual Thread para atender otros hilos.

```mermaid
graph LR
    subgraph Anti-Patrón (Pinning)
        SYNC[synchronized block / Native Method] -->|Bloquea| PINNED[Carrier Thread Pinning!]
    end

    subgraph Patrón Correcto (Non-Pinning)
        LOCK[ReentrantLock.lock()] -->|Desmonta VT durante I/O| FREE[Carrier Thread Libre para otros VTs]
    end
```

### Solución en Java 25: Sustituir `synchronized` por `ReentrantLock`

```java
package com.corp.concurrency;

import java.util.concurrent.locks.ReentrantLock;
import java.time.Duration;

public class ThreadSafeResource {

    private final ReentrantLock lock = new ReentrantLock();

    public void executeCriticalSection() {
        lock.lock();
        try {
            // Operación bloqueante I/O segura sin Carrier Thread Pinning
            Thread.sleep(Duration.ofMillis(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
```

---

## 3. Configuración en Spring Boot 4.0

Para habilitar Virtual Threads de forma global en Spring Boot 4.0 para tomcat, ejecución de tareas asíncronas y programación de tareas:

```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true
```

### Configuración Programática de Executor

```java
package com.corp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadConfig {

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
```
