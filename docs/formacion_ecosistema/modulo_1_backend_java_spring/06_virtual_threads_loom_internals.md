# Módulo 1.6: Virtual Threads (Project Loom) e Internals de Continuaciones

---

## 1. 🐣 Rincón Junior: El Problema de las Ventanillas del Banco

Imagina un banco tradicional (tu servidor Java antiguo). Cada cliente (Petición Web) es atendido por un cajero exclusivo (un Hilo o Thread). Si el cliente dice "voy a revisar mi bolso para buscar la tarjeta" y tarda 5 segundos, el cajero se queda *congelado* mirando al cliente, bloqueando la ventanilla, sin poder atender a nadie más.
Dado que contratar cajeros (crear Hilos del Sistema Operativo) es carísimo y consume mucha memoria (1MB por cajero), si tienes 200 cajeros y todos están esperando a que el cliente busque la tarjeta (esperando a la Base de Datos o a la Red), tu servidor colapsa.

**Virtual Threads (Project Loom en Java 21+)** cambia las reglas:
Ahora tienes millones de "Cajeros Virtuales" baratísimos. Cuando un cliente se pone a buscar la tarjeta (bloqueo I/O), el Cajero Virtual se "desmonta" instantáneamente de la ventanilla física, dejando que *otro* Cajero Virtual use esa misma ventanilla física para atender al siguiente cliente. Cuando el primer cliente encuentra la tarjeta, su Cajero Virtual vuelve a montarse en cualquier ventanilla física libre para continuar.

---

## 2. 🔬 Fundamentos Computacionales: Modelo M:N y Scheduling

A nivel de Sistemas Operativos, existen modelos de hilos:
*   **1:1 (Platform Threads clásicos)**: 1 Hilo Java = 1 Hilo del Kernel (Pthread en Linux). El Kernel hace el cambio de contexto (Context Switch). Es lento (~1-10 microsegundos) porque implica cambiar al Anillo 0 (Kernel Space), guardar registros, TLBs, etc.
*   **M:1 (Green Threads antiguos)**: Múltiples hilos virtuales sobre 1 hilo del SO. Falla miserablemente si hay bloqueos nativos o múltiples núcleos en la CPU.
*   **M:N (Project Loom)**: $M$ Virtual Threads multiplexados sobre $N$ Carrier Threads (Hilos del SO). La propia JVM actúa como un mini-Sistema Operativo en el espacio de usuario (User Space), realizando el cambio de contexto en nanosegundos.

### ForkJoinPool como Scheduler
Los Carrier Threads (las "ventanillas físicas") en Loom son gestionados por un `ForkJoinPool` dedicado. Este pool utiliza un algoritmo matemático de **Work-Stealing** (Robo de Trabajo). Si el Carrier Thread 1 termina todas sus tareas y se queda sin nada que hacer, mirará la cola de tareas del Carrier Thread 2 y le "robará" Virtual Threads por detrás de la cola (Dequeueing), balanceando la carga perfectamente entre todos los núcleos de la CPU (L1/L2 Cache affinity).

---

## 3. 🚀 Arquitectura Práctica: Virtual Threads en Java 25

Usar Virtual Threads es trivial, ya que extienden la misma clase `java.lang.Thread`. Tu código Spring Boot síncrono antiguo (imperativo) mágicamente se vuelve hiper-escalable sin necesidad de aprender flujos asíncronos complejos (Reactor / WebFlux).

```java
// Crear 1 millón de hilos virtuales
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 1_000_000).forEach(i -> {
        executor.submit(() -> {
            // Este sleep NO bloquea el Carrier Thread (Hilo del SO)
            Thread.sleep(Duration.ofSeconds(1));
            System.out.println("Tarea " + i);
            return i;
        });
    });
} // El try-with-resources espera automáticamente (Structured Concurrency) a que el millón termine.
```

---

## 4. 🧠 Internals de Bajo Nivel: Continuaciones (Continuations) en C++

¿Cómo logra la JVM "desmontar" y "montar" un Virtual Thread? Utilizando una estructura profunda escrita en C++ llamada **Continuation**.

Una Continuación (`jdk.internal.vm.Continuation`) es la capacidad de suspender la ejecución de un programa en un punto exacto y reanudarla más tarde.
Cuando un Virtual Thread hace una llamada I/O (ej. `Socket.read()`), la JVM intercepta esa llamada a nivel interno.
1.  **Yield (Ceder)**: La JVM invoca `Continuation.yield()`. El código C++ nativo copia todas las variables locales (los *Stack Frames* de la memoria JVM Stack) de ese Virtual Thread y las traslada al **Heap** como un objeto normal de Java.
2.  **Unmount**: El Carrier Thread queda libre inmediatamente.
3.  **Resume**: Cuando los datos de red llegan (usando `epoll` en Linux), la JVM recibe el evento, busca el objeto en el Heap, copia los Stack Frames de vuelta a un Carrier Thread libre, e invoca `Continuation.run()`, restaurando el Instruction Pointer (PC Register).

¡Copiar unos pocos bytes de Stack al Heap toma nanosegundos, mucho más rápido que un Context Switch del Sistema Operativo!

---

## 5. ⚠️ Runbook SRE y Anti-Patrones: Carrier Thread Pinning

**Incidente**: Activas Virtual Threads en tu aplicación Spring Boot, lanzas 5000 peticiones concurrentes, y el rendimiento se desploma a niveles peores que con hilos clásicos.
**Diagnóstico**: **Carrier Thread Pinning**.

Existen dos situaciones en Java 21-25 donde la JVM **NO PUEDE** desmontar un Virtual Thread del Carrier Thread (se queda pegado/fijado):
1.  Cuando se está ejecutando código nativo a través de JNI/JNA.
2.  **El asesino silencioso**: Cuando el Virtual Thread está ejecutando código dentro de un bloque o método **`synchronized`** clásico, y hace una llamada bloqueante (I/O) dentro de ese bloque. (La JVM está atada internamente a la dirección de memoria del hilo del SO al usar monitores clásicos).

Si 100 Virtual Threads hacen Pinning, agotan los 100 Carrier Threads (basado en el número de núcleos de CPU). Miles de otros Virtual Threads se quedarán en la cola, paralizando la aplicación.

**Solución SRE y Refactorización**:
1. Ejecutar la aplicación con el flag de rastreo: `-Djdk.tracePinnedThreads=full` o `-Djdk.tracePinnedThreads=short`. Esto imprimirá el StackTrace exacto donde ocurre el Pinning.
2. **Reescribir el código**: Reemplazar todo uso de `synchronized` por **`java.util.concurrent.locks.ReentrantLock`**. La JVM moderna sabe cómo desmontar hilos que usan `ReentrantLock` porque están implementados a nivel de Java, no a nivel de monitores nativos de C++.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

Expandimos hacia las implementaciones reales de ensamblador y matemática de grafos distribuidos aplicadas a las continuaciones delimitadas (Delimited Continuations).

## 6. Continuation Stealing vs Thread Stealing

El algoritmo de "Work-Stealing" en el `ForkJoinPool` clásico roba "tareas" completas (`Runnable` o `Callable`) antes de que empiecen. 
Con Virtual Threads, el concepto se expande al **Continuation Stealing**. 

Un Virtual Thread arranca en el Carrier Thread A. Hace un `yield()` por I/O. Sus variables de estado se empaquetan en un `Continuation` en el Heap.
Minutos más tarde, la respuesta I/O llega. El Carrier Thread A está ocupado al 100% (haciendo hash criptográfico, por ejemplo). El Carrier Thread B está ocioso.
El Carrier Thread B inspecciona la cola global (o la cola robable de A), ve el objeto `Continuation` de nuestro Virtual Thread (suspendido en medio de su ejecución), **lo roba, monta el Stack en sus propios registros de hardware, y lo reanuda**.
Para la aplicación, un mismo método se suspendió en la CPU 0 y resucitó mágicamente en la CPU 4, preservando un determinismo estricto de hilos (ThreadLocal variables viajan con el Virtual Thread intactas).

## 7. Disección Assembly del `Yield` Operation (x86_64)

Entender la pausa de un hilo requiere bajar al nivel del C++ en la JVM (`continuationFreezeThaw.cpp`).
Cuando ocurre un `yield()`, la JVM llama a un stub ensamblador nativo: `Continuation::freeze()`.

**Flujo lógico a nivel registros CPU:**
1. **Conservación de Estado Base**: La CPU salva el `RBP` (Base Pointer del Frame actual) y el `RSP` (Stack Pointer).
2. **Cálculo de Delta (Tamaño del Chunk)**: La JVM calcula cuántos bytes hay entre el fondo de la Continuación Delimitada (la base que se marcó con `Continuation.enter()`) y la cima actual del Stack (`RSP`).
3. **Array Copy (Magia Vectorial)**: En lugar de copiar variables una a una, C++ hace un `memcpy` brutalizado (usando instrucciones SIMD AVX-512 si es posible en procesadores modernos) para agarrar todo ese bloque de memoria cruda del Thread Stack y escupirlo directamente en el `byte[]` de un objeto Java alojado en el Heap (`StackChunk`).
4. **Reseteo del Carrier**: El `RSP` (Puntero del Stack) se rebobina violentamente a la marca original, borrando el Virtual Thread de la CPU. El Carrier Thread hace `return` y es devuelto al `ForkJoinPool`.

```nasm
// Pseudocódigo Ensamblador Conceptual del Freeze
push rbp                 // Salvar el frame actual
mov r14, r15_thread      // Cargar puntero al ThreadState C++
call _continuation_freeze_stub // Llamada al runtime C++ pesado
// Si el stub devuelve éxito (se congeló), se altera el Stack Pointer
mov rsp, [r14 + OFFSET_CARRIER_SP] 
ret                      // Retorna "fuera" del Virtual Thread, hacia el bucle del Pool
```

## 8. El Límite de las Continuaciones: Native Frames

La magia de `freeze()` y `thaw()` funciona manipulando punteros del JVM Stack. 
**¿Por qué ocurre el Pinning con JNI?**
Si tu Virtual Thread llama a una función C nativa (ej. la librería de TensorFlow en Python/Java), se crea un "Native C Frame" en el Stack. El C++ de la JVM (el `freeze_stub`) sabe leer e interpretar los Java Frames (sabe dónde están las referencias a objetos para actualizar el GC).
Pero la JVM es completamente **ciega** respecto al diseño interno de un Native C Frame. No sabe si hay punteros de C ocultos allí. Si la JVM intentara copiar (freeze) un Native Frame al Heap y moverlo a otra dirección de memoria, los punteros C estáticos se volverían colgantes (Dangling Pointers), provocando un `Segmentation Fault` fulminante en el SO.
Por tanto, si la JVM detecta *cualquier* Native Frame en el Stack durante un intento de `yield()`, aborta el freeze silenciosamente. El Hilo Virtual se queda "pinned" bloqueando físicamente al Carrier Thread hasta que retorne.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Virtual Threads (Project Loom) e Internals de Continuaciones** a un estudiante de secundaria, **sin usar las palabras:** "Virtual", "Threads", "(Project" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
