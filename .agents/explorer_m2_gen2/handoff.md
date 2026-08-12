# Plan de Remediación y Handoff Report — Hito 2 (Iteración 2): Optimización de SaaSRegantes

**Autor**: Explorador M2 Gen 2 (Teamwork Explorer)  
**Fecha**: 2026-07-29  
**Directorio del Agente**: `/home/jaruiz/Desarrollo/.agents/explorer_m2_gen2`  
**Repositorio Afectado**: `/home/jaruiz/Desarrollo/SaaSRegantes`  
**Informe de Entrada**: `/home/jaruiz/Desarrollo/.agents/auditor_m2/handoff.md`  

---

## 1. Observation (Observaciones Directas con Evidencia de Código)

### 1.1 Hallazgo Forense Confirmado
- En el informe de auditoría forense (`/home/jaruiz/Desarrollo/.agents/auditor_m2/handoff.md`, líneas 60-83), el Auditor Forense detectó una **VIOLACIÓN DE INTEGRIDAD (Integrity Violation)** en la clase:
  - Fichero: `/home/jaruiz/Desarrollo/SaaSRegantes/module-telemetria/src/main/java/com/saasregantes/telemetria/infrastructure/queue/DisruptorTelemetryIngestor.java`
  - En la línea 24:
    ```java
    this.ringBuffer = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    ```
- **Evidencia Técnica**:
  - `java.util.concurrent.ArrayBlockingQueue` de JDK 25 utiliza internamente un cerrojo explícito de reentrada `final ReentrantLock lock`.
  - Cada operación `offer()`, `poll()`, o `drainTo()` en `ArrayBlockingQueue` invoca `lock.lock()`, provocando exclusión mutua síncrona.
  - Esto contradice la afirmación de Javadoc de la clase (Línea 11: *"Ingestor Telemétrico IoT en memoria libre de bloqueos basados en RingBuffer"*) y viola la exigencia de un RingBuffer atómico libre de bloqueos (Lock-Free).

### 1.2 Estado de Componentes SIMD y Webhook
- **`VectorizedH3AuctionEngine.java`** (`module-operacion`): Verificado y marcado como PASS ✅ por el Auditor. Utiliza Java 25 Vector API (`DoubleVector`). Debe conservarse intacto.
- **`VectorizedWaterPhysicsEngine.java`** (`module-mantenimiento`): Verificado y marcado como PASS ✅ por el Auditor. Utiliza Java 25 Vector API (`DoubleVector`). Debe conservarse intacto.
- **`NonBlockingIotWebhookController.java`** (`module-telemetria`): Controlador REST no bloqueante que responde HTTP 202 Accepted en $<1\text{ ms}$. Verificado como PASS ✅ por el Auditor. Sus firmas de método (`telemetryIngestor.offerDirectBuffer` y `telemetryIngestor.offerItem`) deben ser preservadas.

### 1.3 Dependencias del Proyecto
- `module-telemetria/pom.xml` no incluye actualmente la librería `jctools-core`.
- `VectorizedTelemetryBatchWorker.java` (`module-telemetria/src/main/java/com/saasregantes/telemetria/application/service/VectorizedTelemetryBatchWorker.java`) invoca:
  - Línea 61: `ingestor.getRingBuffer().drainTo(batch, BATCH_SIZE);`

---

## 2. Logic Chain (Cadena Lógica de Razonamiento)

1. **Dado que** `DisruptorTelemetryIngestor.java` instanciaba `ArrayBlockingQueue`, la cual usa un `ReentrantLock` en cada operación, la implementación no era Lock-Free y falló la auditoría forense por Violación de Integridad.
2. **Dado que** la ingesta de telemetría IoT presenta una concurrencia de tipo **Multi-Productor / Single-Consumidor (MPSC)** (múltiples hilos HTTP de controlador encolando datos y 1 Virtual Thread worker consumiendo), la estructura óptima de RingBuffer libre de bloqueos es una cola atómica de capacidad fija potencia de 2 ($N = 131.072 = 2^{17}$) basada en instrucciones CAS (Compare-And-Swap) atómicas sin cerrojos.
3. **Dado que** existen dos vías idóneas para implementar este RingBuffer Lock-Free:
   - **Opción A (Librería Estándar de Alto Rendimiento JCTools `org.jctools.queues.MpscArrayQueue`)**: Ofrece un RingBuffer MPSC de capacidad fija potencia de 2, padding de cache lines (evita false sharing) y cero cerrojos.
   - **Opción B (Implementación Atómica CAS Nativa `LockFreeRingBuffer<E>`)**: Implementación propia en Java 25 con `AtomicReferenceArray<E>` y punteros de secuencia atómicos `AtomicLong` (CAS), cero dependencias externas.
4. **Se concluye que** sustituir `ArrayBlockingQueue` por `MpscArrayQueue` (o `LockFreeRingBuffer`) y adaptar `DisruptorTelemetryIngestor` para exponer un método `drainTo(...)` directo resolverá la Violación de Integridad, eliminando los cerrojos `ReentrantLock` sin alterar los motores SIMD ni el controlador no bloqueante.

---

## 3. Blueprint Técnico de Remediación (Instrucciones para el Worker M2 Gen 2)

El Worker de la Iteración 2 debe aplicar exactamente los siguientes cambios en `SaaSRegantes`:

### Cambio 1: Añadir Dependencia JCTools en `module-telemetria/pom.xml` (Opción A)
En `/home/jaruiz/Desarrollo/SaaSRegantes/module-telemetria/pom.xml`, añadir en la sección `<dependencies>`:

```xml
<dependency>
    <groupId>org.jctools</groupId>
    <artifactId>jctools-core</artifactId>
    <version>4.0.5</version>
</dependency>
```

---

### Cambio 2: Refactorizar `DisruptorTelemetryIngestor.java`
En `/home/jaruiz/Desarrollo/SaaSRegantes/module-telemetria/src/main/java/com/saasregantes/telemetria/infrastructure/queue/DisruptorTelemetryIngestor.java`:

Reemplazar el contenido completo por:

```java
package com.saasregantes.telemetria.infrastructure.queue;

import com.saasregantes.telemetria.infrastructure.adapter.TelemetryBatchFlatBufferAdapter;
import org.jctools.queues.MpscArrayQueue;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Queue;

/**
 * Ingestor Telemétrico IoT en memoria LIBRE DE BLOQUEOS (Lock-Free MPSC RingBuffer).
 * Utiliza MpscArrayQueue de JCTools con punteros atómicos CAS y 128k slots (2^17).
 * Cero cerrojos ReentrantLock ni bloques synchronized.
 */
@Component
public class DisruptorTelemetryIngestor {

    private static final int QUEUE_CAPACITY = 131_072; // 128k slots (2^17)
    private final MpscArrayQueue<TelemetryBatchFlatBufferAdapter.TelemetryItem> ringBuffer;
    private final TelemetryBatchFlatBufferAdapter flatBufferAdapter;

    public DisruptorTelemetryIngestor(TelemetryBatchFlatBufferAdapter flatBufferAdapter) {
        this.flatBufferAdapter = flatBufferAdapter;
        this.ringBuffer = new MpscArrayQueue<>(QUEUE_CAPACITY);
    }

    /**
     * Ofrece un buffer de memoria binario para ser encolado sin bloqueos (Lock-Free CAS).
     */
    public boolean offerDirectBuffer(ByteBuffer buffer) {
        if (buffer == null) return false;
        TelemetryBatchFlatBufferAdapter.BatchHeader header = flatBufferAdapter.decodeZeroCopy(buffer);
        if (header.batchSize() == 0) return true;

        boolean allAdded = true;
        for (var item : header.items()) {
            boolean added = ringBuffer.offer(item);
            if (!added) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    /**
     * Ofrece un item telemétrico directo al RingBuffer sin bloqueos.
     */
    public boolean offerItem(TelemetryBatchFlatBufferAdapter.TelemetryItem item) {
        if (item == null) return false;
        return ringBuffer.offer(item);
    }

    /**
     * Drena hasta maxElements elementos directamente desde el RingBuffer lock-free.
     */
    public int drainTo(Collection<? super TelemetryBatchFlatBufferAdapter.TelemetryItem> c, int maxElements) {
        return ringBuffer.drain(c::add, maxElements);
    }

    public Queue<TelemetryBatchFlatBufferAdapter.TelemetryItem> getRingBuffer() {
        return ringBuffer;
    }

    public int getQueueSize() {
        return ringBuffer.size();
    }
}
```

---

### Cambio 3: Actualizar Consumidor en `VectorizedTelemetryBatchWorker.java`
En `/home/jaruiz/Desarrollo/SaaSRegantes/module-telemetria/src/main/java/com/saasregantes/telemetria/application/service/VectorizedTelemetryBatchWorker.java`:

Modificar la línea 61 en el método `processLoop()`:
- **Antes**: `ingestor.getRingBuffer().drainTo(batch, BATCH_SIZE);`
- **Después**: `ingestor.drainTo(batch, BATCH_SIZE);`

---

### Opción B Alternativa (Si se prefiere una solución Lock-Free en Java Puro sin librerías externas)
Si por cualquier restricción no se desea usar `jctools-core`, se puede incluir una clase `LockFreeRingBuffer.java` en `com.saasregantes.telemetria.infrastructure.queue`:

```java
package com.saasregantes.telemetria.infrastructure.queue;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * RingBuffer Lock-Free MPSC basado en AtomicReferenceArray y punteros atómicos CAS (AtomicLong).
 * Capacidad fija potencia de 2 (ej. 131.072 slots).
 */
public class LockFreeRingBuffer<E> extends AbstractQueue<E> {

    private final int capacity;
    private final int mask;
    private final AtomicReferenceArray<E> buffer;
    private final AtomicLong producerIndex = new AtomicLong(0);
    private final AtomicLong consumerIndex = new AtomicLong(0);

    public LockFreeRingBuffer(int capacity) {
        if (Integer.bitCount(capacity) != 1) {
            throw new IllegalArgumentException("La capacidad debe ser una potencia de 2");
        }
        this.capacity = capacity;
        this.mask = capacity - 1;
        this.buffer = new AtomicReferenceArray<>(capacity);
    }

    @Override
    public boolean offer(E item) {
        if (item == null) return false;
        while (true) {
            long pIndex = producerIndex.get();
            long cIndex = consumerIndex.get();
            if (pIndex - cIndex >= capacity) {
                return false; // Cola llena
            }
            if (producerIndex.compareAndSet(pIndex, pIndex + 1)) {
                int index = (int) (pIndex & mask);
                buffer.set(index, item);
                return true;
            }
        }
    }

    @Override
    public E poll() {
        while (true) {
            long cIndex = consumerIndex.get();
            long pIndex = producerIndex.get();
            if (cIndex >= pIndex) {
                return null; // Cola vacía
            }
            int index = (int) (cIndex & mask);
            E item = buffer.get(index);
            if (item != null) {
                if (buffer.compareAndSet(index, item, null)) {
                    consumerIndex.lazySet(cIndex + 1);
                    return item;
                }
            } else {
                Thread.onSpinWait();
            }
        }
    }

    @Override
    public E peek() {
        long cIndex = consumerIndex.get();
        int index = (int) (cIndex & mask);
        return buffer.get(index);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        int count = 0;
        while (count < maxElements) {
            E item = poll();
            if (item == null) break;
            c.add(item);
            count++;
        }
        return count;
    }

    @Override
    public int size() {
        long p = producerIndex.get();
        long c = consumerIndex.get();
        return Math.max(0, (int) (p - c));
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Operación no soportada");
    }
}
```

---

## 4. Caveats (Previsión de Limitaciones y Asunciones)

- **Capacidad Potencia de 2**: `131.072` es $2^{17}$, lo cual cumple el requisito estricto de optimización mediante máscara binaria (`index & (capacity - 1)`).
- **MPSC vs MPMC**: La arquitectura IoT de SaaSRegantes tiene múltiples hilos concurrentes produciendo (los controladores HTTP de webhook) y un único hilo consumidor en `VectorizedTelemetryBatchWorker`. `MpscArrayQueue` es óptimo para este perfil de tráfico.
- **Preservación Total**: Los componentes `VectorizedH3AuctionEngine.java` y `VectorizedWaterPhysicsEngine.java` no requieren ninguna modificación.

---

## 5. Conclusion (Conclusión y Recomendación Final)

La aplicación de la **Opción A (JCTools `MpscArrayQueue`)** o la **Opción B (`LockFreeRingBuffer` con CAS nativo)** garantiza la erradicación completa de cerrojos `ReentrantLock` en el pipeline de ingesta telemétrica de `SaaSRegantes`, convirtiendo `DisruptorTelemetryIngestor` en un RingBuffer 100% Lock-Free auténtico.

---

## 6. Verification Method (Método de Verificación Independiente)

El Auditor o el Worker pueden verificar la remediación ejecutando los siguientes pasos:

1. **Inspección de Ausencia de Cerrojos**:
   ```bash
   grep -E "ArrayBlockingQueue|ReentrantLock|synchronized" /home/jaruiz/Desarrollo/SaaSRegantes/module-telemetria/src/main/java/com/saasregantes/telemetria/infrastructure/queue/DisruptorTelemetryIngestor.java
   ```
   *Criterio de Éxito*: 0 coincidencias en código ejecutable.

2. **Ejecución de Pruebas de Módulo de Telemetría**:
   ```bash
   cd /home/jaruiz/Desarrollo/SaaSRegantes
   mvn test -pl module-telemetria
   ```
   *Criterio de Éxito*: `DisruptorTelemetryIngestorTest` y `IotPipelineConcurrencyEmpiricalStressTest` ejecutan con `BUILD SUCCESS`.

3. **Ejecución de Pruebas Integrales de Módulos Modificados**:
   ```bash
   cd /home/jaruiz/Desarrollo/SaaSRegantes
   mvn test -pl module-operacion,module-mantenimiento,module-telemetria
   ```
   *Criterio de Éxito*: Todos los tests (incluyendo motores SIMD y stress test concurrente) pasan al 100%.
