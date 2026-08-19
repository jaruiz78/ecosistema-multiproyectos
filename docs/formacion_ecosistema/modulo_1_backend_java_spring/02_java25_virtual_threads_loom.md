# Módulo 1 - Lección 2: Concurrencia Masiva con Java 25 & Virtual Threads (Loom)

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué son los Virtual Threads (Project Loom)?
Imagina un call center con 8 operadores de teléfono (los **Hilos del Sistema Operativo / Platform Threads**). Si cada cliente que llama exige que un operador se quede en silencio esperando 5 minutos a que busque un documento (**I/O bloqueante**), el call center se colapsa con solo 8 llamadas simultáneas.

Los **Virtual Threads (Hilos Virtuales)** son como dar a los operadores una pantalla digital donde pueden atender miles de llamadas pendientes a la vez: cuando una llamada está esperando en silencio (I/O bloqueante), el operador suelta temporalmente esa llamada y atiende otra al instante.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Espacio de Usuario / JVM Java 25
        VT1["Virtual Thread 1 - I/O Blocked"]
        VT2[Virtual Thread 2 - Running]
        VT3[Virtual Thread 3 - Running]
        VT4[Virtual Thread 4 - Waiting DB]
    end

    subgraph Carrier Threads / Hilos de Plataforma (OS Threads)
        CT1[Carrier Thread A]
        CT2[Carrier Thread B]
    end

    subgraph Núcleos de CPU del Sistema Operativo
        CPU1[Core CPU 1]
        CPU2[Core CPU 2]
    end

    VT2 -->|Montado en| CT1
    VT3 -->|Montado en| CT2
    CT1 --> CPU1
    CT2 --> CPU2
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

### Paso 1: Configurar Virtual Threads en Spring Boot 4.1

```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true
```

### Paso 2: Usar Executors de Virtual Threads en Java Puro

```java
package com.corp.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadRunner {

    public static void main(String[] args) {
        // Genera un executor que crea un hilo virtual ligero por cada tarea (Zero thread pool size limit)
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10_000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // Operación I/O bloqueante (Simulada)
                    Thread.sleep(1000);
                    return "Tarea " + taskId + " completada";
                });
            }
        } // El try-with-resources espera automáticamente a que terminen las 10,000 tareas
    }
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Comparativa de Hilos

| Propiedad | Hilo de Plataforma (OS Thread) | Virtual Thread (Java 25) |
| :--- | :--- | :--- |
| **Tamaño de Pila en RAM** | ~1 MB estático asignado por OS | **~1 KB dinámico en Heap** |
| **Tiempo de Creación** | ~1 ms (Syscall al kernel) | **~10 ns (Objeto Java ligero)** |
| **Límite de Concurrencia** | ~2,000 por proceso JVM | **> 1,000,000 por proceso JVM** |
| **Carrier Thread Pinning** | N/A | Ocurre en bloques `synchronized` |

### Diagnóstico de Carrier Thread Pinning
Bandera de la JVM para detectar si los Virtual Threads se quedan enganchados sin poder desmontarse:

```bash
java -Djdk.tracePinnedThreads=full -jar app.jar
```

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Hacer Pool de Virtual Threads (`newFixedThreadPool(100)`)**:
   * *Síntoma*: Limitar deliberadamente los Virtual Threads arruina su diseño. Nunca debes crear un pool de hilos virtuales.
   * *Solución*: Crea siempre un nuevo Virtual Thread por cada tarea concurrente con `Executors.newVirtualThreadPerTaskExecutor()`.
2. **Utilizar bloques `synchronized` envolviendo llamadas a I/O (DB, HTTP)**:
   * *Síntoma*: Produce *Carrier Thread Pinning*, bloqueando el hilo de plataforma subyacente.
   * *Solución*: Reemplaza `synchronized` por `java.util.concurrent.locks.ReentrantLock`.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Concurrencia Masiva con Java 25 & Virtual Threads (Loom)** a un estudiante de secundaria, **sin usar las palabras:** "Concurrencia", "Masiva", "con" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

## ⚙️ Primeros Principios & Fundamentos Conceptuales
1. **Descomposición Atómica:** Cada componente en Módulo 1 - Lección 2: Concurrencia Masiva con Java 25 & Virtual Threads (Loom) se modela de forma determinista y sin estado mutable compartido.
2. **Invariante de Dominio:** Los estados del sistema transicionan exclusivamente a través de funciones puras e interfaces selladas.
3. **Cero Suposiciones:** No se asume fiabilidad de red ni memoria infinita; cada llamada maneja explícitamente fallos y límites de cuota.

