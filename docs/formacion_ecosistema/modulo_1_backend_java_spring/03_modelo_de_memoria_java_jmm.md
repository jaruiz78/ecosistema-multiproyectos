# Módulo 1.3: Modelo de Memoria de Java (JMM) y Coherencia de Caché (Nivel Stanford)

---

## 1. 🐣 Rincón Junior: ¿Por qué mi variable desaparece?

Imagina que dos cocineros (Hilos) trabajan en la misma cocina (Memoria). El Cocinero A pone la sal en la mesa. El Cocinero B, que está distraído, no vio al Cocinero A poner la sal, así que asume que no hay sal.
En programación multihilo, las CPUs modernas hacen exactamente esto: copian variables a su propia memoria "rápida" (caché) para no tener que caminar hasta la mesa principal (RAM). Si el Hilo A cambia una variable en su caché, el Hilo B no lo verá inmediatamente a menos que los obligues a "sincronizar" sus versiones. El **Java Memory Model (JMM)** es el libro de reglas que dicta exactamente cuándo y cómo un cocinero debe gritar "¡He cambiado la sal!" para que los demás lo vean.

---

## 2. 🔬 Fundamentos Computacionales: Coherencia de Caché (MESI)

En arquitectura de hardware real, la memoria principal (DRAM) es lentísima (~100 nanosegundos de latencia) comparada con la CPU (~0.5 ns). Para compensar, las CPUs modernas (Intel, AMD, ARM) tienen múltiples capas de caché (L1, L2 por núcleo; L3 compartida).

Cuando el Hilo 1 (Core 1) lee la variable `x`, la trae a su Caché L1. Si la modifica, la caché del Core 1 se vuelve *Dirty* (sucia). El Core 2 (ejecutando el Hilo 2) todavía tiene la versión antigua de `x` en su Caché L1 (*Stale*).

El hardware resuelve esto mediante **Protocolos de Coherencia de Caché**, siendo **MESI** el más famoso. Cada línea de caché (generalmente de 64 bytes) está en uno de 4 estados:
*   **M (Modified)**: Esta caché tiene la única copia válida. La memoria RAM está obsoleta.
*   **E (Exclusive)**: Esta caché tiene la única copia, y coincide exactamente con la RAM.
*   **S (Shared)**: Varios núcleos tienen copias idénticas.
*   **I (Invalid)**: El dato en esta caché ya no sirve porque otro núcleo lo modificó.

El hardware envía mensajes por el bus de la placa base para mantener este estado. ¡Pero el JMM impone reglas *por encima* de esto!

---

## 3. 🚀 El Java Memory Model (JMM) y Happens-Before

El JMM no es una pieza de software; es una especificación formal. Su concepto central es la relación **Happens-Before** (Sucede-Antes).

Si una acción A *happens-before* una acción B, el JMM **garantiza** que B verá los efectos de A.

### Reglas clave de Happens-Before:
1.  **Program Order Rule**: Cada acción en un hilo *happens-before* toda acción subsecuente en el mismo hilo.
2.  **Monitor Lock Rule**: Un `unlock` en un *monitor* (bloque `synchronized`) *happens-before* un subsecuente `lock` en ese mismo monitor. (Garantiza visibilidad entre hilos).
3.  **Volatile Variable Rule**: Una escritura en una variable `volatile` *happens-before* toda subsecuente lectura de esa misma variable `volatile`.
4.  **Thread Start Rule**: Llamar a `Thread.start()` *happens-before* cualquier instrucción ejecutada dentro del nuevo hilo.

---

## 4. 🧠 Internals: Barreras de Memoria (Memory Barriers/Fences)

¿Cómo hace la JVM para obligar a la CPU física a respetar el JMM? Usando instrucciones de ensamblador llamadas **Memory Fences**.

Los compiladores JIT y las CPUs modernas son tan agresivos optimizando que rutinariamente **reordenan** tus instrucciones (Instruction Reordering). Si escribes `a=1; b=2;`, la CPU podría ejecutar `b=2` primero si los datos no dependen entre sí, porque es más eficiente para su pipeline.

Para variables `volatile`, la JVM inserta barreras (ej. instrucción `mfence` en x86, o `dmb` en ARM) que prohíben reordenamientos cruzando la barrera:
*   **LoadLoad**: Lecturas posteriores no pueden adelantarse a lecturas anteriores.
*   **StoreStore**: Escrituras posteriores no pueden adelantarse a escrituras anteriores.
*   **LoadStore**: Escrituras posteriores no pueden adelantarse a lecturas anteriores.
*   **StoreLoad** (La más pesada): Vacía los buffers de escritura de la CPU (Store Buffers) directamente a la caché/RAM, forzando visibilidad global. Una escritura a `volatile` emite un StoreLoad.

---

## 5. ⚡ El Peligro del Doble Checking Locking (DCL) Antiguo

Un ejemplo clásico de fallo del JMM en Java antiguo (pre-Java 1.5) era el Singleton con Double-Checked Locking sin `volatile`.

```java
// ANTI-PATRÓN PELIGROSO SI 'instance' NO ES VOLATILE
public class SingletonPeligroso {
    private static SingletonPeligroso instance;

    public static SingletonPeligroso getInstance() {
        if (instance == null) { // Lectura 1
            synchronized (SingletonPeligroso.class) {
                if (instance == null) {
                    instance = new SingletonPeligroso(); // EL PELIGRO ESTÁ AQUÍ
                }
            }
        }
        return instance;
    }
}
```

**¿Por qué falla a nivel de CPU?**
La instrucción `instance = new SingletonPeligroso();` no es atómica. El compilador JIT la divide en 3 pasos en ensamblador:
1. Asignar memoria para el objeto (`malloc` / `TLAB`).
2. Llamar al constructor (inicializar campos a valores reales).
3. Asignar la dirección de memoria a la variable `instance`.

Debido al *Reordering*, la CPU podría ejecutar el Paso 3 *antes* del Paso 2. 
Si el Hilo A hace 1 -> 3, y es pausado por el SO. El Hilo B entra, ve que `instance` != `null` (Lectura 1), y retorna un objeto *parcialmente construido* (campos nulos o basura).

**Solución**: Declarar `private static volatile SingletonPeligroso instance;`. El JMM fuerza una barrera `StoreStore` que prohíbe reordenar el paso 3 antes del paso 2.

---

## 6. ⚠️ Runbook de Producción: Data Races y Visibilidad

**Incidente**: Un sistema de control de tráfico aéreo marca vuelos como "cancelados" (`boolean isCancelled = true`), pero los hilos de notificación siguen enviando alertas porque leen `false` indefinidamente.

**Causa Raíz**: Ausencia de barreras de memoria. El hilo lector cargó `isCancelled` en un registro del procesador en el bucle `while(!isCancelled)` y, al no ser `volatile`, el compilador JIT optimizó el código convirtiéndolo en un bucle infinito `while(true)` (Hoisting), asumiendo erróneamente que nadie más cambiaría la variable.

**Diagnóstico SRE**:
1. Analizar el código problemático en busca de variables primitivas de bandera (flags) modificadas por múltiples hilos sin sincronización explícita.
2. Comprobar si el hilo lector está atrapado en CPU al 100% (usando `top -H -p <pid>`).

**Solución**: Usar `volatile boolean isCancelled`, o mejor, la clase atómica segura de hardware `AtomicBoolean`.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

El JMM no existe en un vacío; es un puente abstracto sobre las idiosincrasias de arquitecturas de hardware dramáticamente diferentes. 

Para un ingeniero de plataforma (Principal/Staff) escribiendo infraestructuras de ultra-baja latencia (como bases de datos distribuidas o motores de trading), entender la diferencia entre TSO (Total Store Ordering) de x86_64 y el modelo Weak Memory de ARM64 (Graviton/Apple Silicon) es un requisito sine qua non.

## 7. Modelos de Memoria del Hardware: x86_64 (TSO) vs ARM64 (Weak)

El hardware subyacente impone sus propias reglas de reordenamiento antes de que el compilador JIT incluso participe.

### x86_64: Fuerte pero Engañoso (Total Store Ordering)
La arquitectura x86_64 (Intel/AMD) provee un modelo de memoria relativamente fuerte llamado **TSO (Total Store Ordering)**. 
En x86_64:
- Una lectura NO puede ser reordenada con una lectura anterior.
- Una escritura NO puede ser reordenada con una escritura anterior.
- Una lectura NO puede ser reordenada con una escritura anterior.
- **UNA ESCRITURA SÍ puede ser reordenada con una lectura posterior (StoreLoad Reordering).**

Esto significa que, en x86_64, el JIT de Java *apenas necesita emitir barreras en ensamblador* para `volatile`, excepto la infame `StoreLoad` barrier (que en x86 suele implementarse con la instrucción atómica `lock addl `$0`,0(%rsp)` o `mfence`). El hardware x86 perdona muchos errores de concurrencia lógicos. Código mal escrito sin `volatile` a menudo "parece funcionar" en x86.

### ARM64 / Apple Silicon: Weak Memory Model (Relajado)
La arquitectura ARM, predominante hoy en Cloud Run (AWS Graviton, GCP Tau T2A) y terminales móviles, usa un **Weak Memory Model**.
En ARM64:
- Lecturas y escrituras pueden ser reordenadas libremente por la CPU en *cualquier* dirección si no afectan a la misma dirección de memoria.
- La CPU ejecutará especulativamente instrucciones de memoria basándose en predictores de salto.

Código concurrente defectuoso que sobrevivió años en producción sobre x86 **explotará catastróficamente** al migrar a ARM64. Data races latentes que el TSO de Intel tapaba, saldrán a la luz porque el chip ARM reordenará agresivamente operaciones de inicialización.

La JVM en ARM64 tiene que emitir explícitamente instrucciones de hardware `dmb` (Data Memory Barrier) o `isb` (Instruction Synchronization Barrier) o utilizar las nuevas instrucciones ARMv8 atómicas (`ldar` - Load-Acquire / `stlr` - Store-Release) para mapear las reglas del JMM.

## 8. El Papel de los Store Buffers y el Fenómeno "StoreLoad"

El reordenamiento StoreLoad (el único permitido en x86) ocurre por los **Store Buffers** físicos adheridos a cada Core.

1. Cuando un Core escribe en una variable (Store), no espera los ~100ns a que llegue a RAM o incluso a la Caché L1 (que podría requerir invalidar el bus MESI y esperar Ack).
2. Pone la escritura en su "Store Buffer" privado, y sigue ejecutando instrucciones.
3. Si inmediatamente hace una Lectura (Load) de *otra* variable, lee desde su Caché L1.
4. Cronológicamente, el Load ocurrió mientras el Store todavía estaba atrapado en el Buffer privado. El resto del mundo vio el Load antes que el Store.

> [!CAUTION]
> **El Algoritmo de Dekker Roto**
> El famoso algoritmo de exclusión mutua de Dekker falla inherentemente en x86_64 moderno sin barreras explícitas de memoria debido a este reordenamiento StoreLoad de los Store Buffers. 

## 9. Primitivas Avanzadas de Java 25: VarHandles y Memory Ordering C++11

En Java moderno, `sun.misc.Unsafe` está siendo erradicado. La forma "correcta" de manejar memoria atómica sin el coste total de un `volatile` tradicional (que emite StoreLoad total) es usando **VarHandles** (introducidos en Java 9, perfeccionados en Java 25).

VarHandles adoptan la semántica C++11 Memory Model, permitiendo un control granular:

```java
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class UltraLowLatencyQueue {
    private int tail = 0;
    private static final VarHandle TAIL_HANDLE;

    static {
        try {
            TAIL_HANDLE = MethodHandles.lookup().findVarHandle(UltraLowLatencyQueue.class, "tail", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void encolar(int item) {
        // En lugar de hacer 'tail' volatile (que emite Fences completos caros),
        // usamos Acquire/Release semantics.
        // Opaque: Prohíbe optimización del compilador, pero no emite Fences de hardware fuertes.
        // Ideal para Spin-Locks en procesadores x86.
        TAIL_HANDLE.setOpaque(this, tail + 1); 
    }
}
```
Tipos de acceso en VarHandle:
1. **Plain**: Acceso normal. Reordenable.
2. **Opaque**: No se reordena por el compilador, pero sin garantías de barrera de hardware pesada.
3. **Acquire/Release**: Sincronización direccional (una escritura *Release* hace visibles las previas a un hilo lector que haga *Acquire*).
4. **Volatile**: Full memory fence. Más seguro, más lento.

Dominar VarHandles es la frontera que separa a un desarrollador Senior de un Ingeniero Principal de Plataforma (Platform Engineer) capaz de escribir conectores de bases de datos de alta contención.
