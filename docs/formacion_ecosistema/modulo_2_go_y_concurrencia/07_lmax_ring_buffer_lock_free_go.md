# 🐹 Cátedra de Concurrencia Go CSP, Ring-Buffers LMAX & Lock-Free (Nivel ITMO / Peking University)
## *Facultad IV: Scheduler M:N, Ring-Buffers Lock-Free, Atomics y Caché Line Alignment en Go 1.26*

---

### 🏛️ 1. Arquitectura del Scheduler de Go (M:N Work Stealing)

El runtime de Go desacopla los hilos del sistema operativo de las tareas concurrentes mediante un modelo triple:

- **\(G\) (Goroutine)**: Estado de ejecución ultraligero con stack dinámico inicial de \(2\text{ KB}\).
- **\(M\) (Machine / OS Thread)**: Hilo nativo gestionado por el kernel del sistema operativo.
- **\(P\) (Processor Lógico)**: Recurso de cómputo necesario para ejecutar código Go (`GOMAXPROCS`).

```mermaid
graph TD
    subgraph Scheduler ["Go Runtime Scheduler (P -> Local Queue)"]
        P1["P0 (Procesador Lógico)"] --> Q1["Cola Local (256 Gs)"]
        P2["P1 (Procesador Lógico)"] --> Q2["Cola Local (256 Gs)"]
        GQ["Cola Global de Goroutines (Lock-Protected)"]
    end

    subgraph Hardware ["Cores Físicos & Machine Threads"]
        M1["M0 (OS Thread)"] --- P1
        M2["M1 (OS Thread)"] --- P2
    end

    Q1 -.->|Work Stealing (50% de Gs)| Q2
```

#### Algoritmo de Work Stealing
Cuando la cola local de un procesador \(P_i\) se agota:
1. Inspecciona su propia cola local de ejecución.
2. Si está vacía, consulta la cola global cada 61 ticks.
3. Intenta **robar la mitad** (\(50\%\)) de las goroutines de la cola de otro procesador \(P_j\) elegido pseudoaleatoriamente mediante operaciones atómicas CAS (*Compare-And-Swap*).

---

### ⚡ 2. Ring-Buffer Lock-Free (Patrón LMAX Disruptor en Go)

Para lograr un rendimiento de **decenas de millones de mensajes por segundo** con ultrabaja **latencia** sub-microsegundo en el BFF [`pctMultiMicroservices`](file:///home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices), se implementa el patrón **disruptor** mediante un **ring-buffer** **lock-free** basado en operaciones **atomics** y alineamiento estricto a **cache line**.

#### Erradicación de False Sharing (Alineamiento a 64 Bytes de Cache Line)
En arquitecturas x86_64 y ARM64, la **cache line** mide \(64\text{ bytes}\). Dos variables atómicas adyacentes en la misma **cache line** provocan invalidaciones cruzadas de caché L1/L2 (*False Sharing* / *Cache Ping-Pong*). El patrón **disruptor** **lock-free** aísla los cursores de lectura y escritura para garantizar la máxima velocidad y mínima **latencia**.

```go
package disruptor

import (
	"sync/atomic"
	"unsafe"
)

// CacheLinePad evita el False Sharing aislando el cursor a 64 bytes exactos
type Cursor struct {
	_ [8]uint64 // 64 bytes de padding anterior
	value uint64
	_ [7]uint64 // 56 bytes de padding posterior
}

type RingBuffer struct {
	buffer   []unsafe.Pointer
	mask     uint64
	capacity uint64
	cursor   Cursor
	gating   Cursor
}

func NewRingBuffer(capacity uint64) *RingBuffer {
	// Capacidad obligatoria como potencia de 2 para indexación bitwise O(1)
	if capacity&(capacity-1) != 0 {
		panic("Capacity must be a power of 2")
	}
	return &RingBuffer{
		buffer:   make([]unsafe.Pointer, capacity),
		mask:     capacity - 1,
		capacity: capacity,
	}
}

func (rb *RingBuffer) Publish(item unsafe.Pointer) uint64 {
	seq := atomic.AddUint64(&rb.cursor.value, 1)
	index := seq & rb.mask // O(1) bitwise AND en lugar de módulo %
	atomic.StorePointer(&rb.buffer[index], item)
	return seq
}
```

---

### 💡 3. Analogía Feynman (El Carrusel de Alta Velocidad)

* **Metáfora del Carrusel Giratorio:**
  Imagina un carrusel mecánico circular gigante con 1.024 asientos numerados. Los productores depositan cajas en los asientos y los consumidores las recogen mientras el carrusel gira sin detenerse jamás. En lugar de poner un guardia de seguridad con un candado en cada caja (bloqueo `sync.Mutex`), cada trabajador lleva un contador en su mano y sabe exactamente a qué asiento debe acudir mediante una simple operación matemática con el residuo binario (\(\text{seq} \ \& \ \text{mask}\)). Nadie espera a nadie y el rendimiento alcanza el límite físico del metal de la CPU.

---

### 📚 Bibliografía de Cátedra
- Hoare, C. A. R. (1978). *Communicating Sequential Processes*. CACM.
- Thompson, M., Barker, M., et al. (2011). *LMAX Disruptor: High performance alternative to bounded queues for exchanging data between threads*.
- Pike, R. (2012). *Go Concurrency Patterns*. Google I/O.
- Drepper, U. (2007). *What Every Programmer Should Know About Memory*. Red Hat.
