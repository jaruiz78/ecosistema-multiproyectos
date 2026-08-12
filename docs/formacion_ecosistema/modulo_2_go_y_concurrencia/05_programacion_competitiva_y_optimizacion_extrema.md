# Módulo 2.5: Algoritmia Competitiva y Optimización Extrema en Go (Nivel ITMO / Warsaw / Peking)

---

## 1. 🐣 Rincón Junior: Más Allá de los Bucles `for`

Imagina que tienes una base de datos en memoria con 10 millones de viajes de taxi, ordenados por hora. Te preguntan: *"¿Cuántos viajes se hicieron entre las 12:15 y las 18:45?"*.
Un programador Junior haría un bucle `for` que pase por todos los viajes en ese rango horario contándolos uno a uno. Tarda 5 milisegundos. Parece rápido.
Pero si la app de AppViajes recibe 10.000 peticiones por segundo preguntando cosas similares, ese bucle `for` colapsará la CPU y derretirá el servidor.
Los campeones del mundo en programación (Universidades ITMO en Rusia, Warsaw en Polonia, Peking en China) no usan bucles `for` para esto. Usan estructuras de datos donde esa pregunta se responde en **10 nanosegundos**, sin importar si preguntas por 1 hora o por 10 años.
Aprenderemos cómo estas técnicas matemáticas de los mundiales de programación (ICPC) se aplican a los microservicios corporativos de ultra-baja latencia.

---

## 2. 🔬 Fundamentos Teóricos: Estructuras de Datos de Árbol (Segment & Fenwick Trees)

Las operaciones clásicas en bases de datos (SQL) son lentas para consultas matemáticas sobre rangos masivos en tiempo real. 

### Fenwick Tree (Binary Indexed Tree - BIT)
Inventado por Peter Fenwick. Si tienes un array de 1 millón de elementos y cambias el valor del elemento 5, y luego preguntas por la suma total, un array normal sufre.
El Fenwick Tree es un array mágico donde sumar un rango o actualizar un elemento cuesta tiempo logarítmico **$O(\log N)$**.
Se basa en matemáticas binarias (operación bitwise). Cada índice del array guarda la suma de una "responsabilidad" de números anteriores basada en su bit menos significativo (`x & -x`).

### Segment Tree (Árbol de Segmentos)
Más poderoso que el Fenwick Tree. Permite responder a preguntas sobre rangos arbitrarios (ej. *¿Cuál es el valor máximo en el rango [L, R]?*) en $O(\log N)$ y actualizar rangos enteros a la vez usando *Lazy Propagation*.
En el Gemelo Digital, usamos Segment Trees en memoria en el worker de Go para calcular precios dinámicos (Surge) a lo largo del tiempo, donde una promoción de lluvia aplica un multiplicador de x1.5 a todas las celdas H3 de una región simultáneamente.

---

## 3. 🚀 Arquitectura Práctica: SIMD en Go (Single Instruction, Multiple Data)

Las CPUs modernas (Intel/AMD/ARM) tienen registros vectoriales (AVX2, AVX-512) que pueden procesar múltiples datos a la vez. En lugar de sumar `A+B`, pueden sumar 16 números con otros 16 números en un **solo ciclo de reloj**.

Go, por defecto, es seguro y no siempre autovectoriza el código eficientemente como lo hace C++ (gcc/clang). 
Para algoritmos críticos (ej. cálculo de distancias Haversine masivas entre taxis y usuarios), la ingeniería pura recurre a **SIMD**.

**Cómo aplicar SIMD en Go**:
No escribimos ensamblador manualmente. Usamos librerías (ej. `github.com/segmentio/asm`) o ensamblamos funciones `.s` llamadas desde Go (Go Assembly).
El flujo es:
1.  Cargar 8 coordenadas GPS (float64) en un solo registro YMM (256 bits).
2.  Ejecutar la instrucción matemática vectorial.
3.  Guardar los 8 resultados de vuelta en RAM.
*Latencia reducida geométricamente por un factor de 8x.*

---

## 4. 🧠 Internals Avanzados: Estructuras Lock-Free y Heavy-Light Decomposition

### Heavy-Light Decomposition (HLD)
Imagina el grafo de la red eléctrica corporativa de un país. Quieres saber el voltaje mínimo en la ruta entre la subestación de Madrid y la de Barcelona.
Hacer un BFS/DFS en cada consulta destruiría el Throughput.
HLD es una técnica de programación competitiva (usada en la ICPC) que toma un árbol y lo corta en "cadenas pesadas" y "ligeras" matemáticamente óptimas. Permite transformar operaciones complejas sobre árboles en simples operaciones sobre Segment Trees planos. Reduciendo la latencia de $O(N)$ a **$O(\log^2 N)$**. Esto se usa en el motor `tensor_gnn_core` para validar las restricciones de red antes de ejecutar el optimizador PyPSA.

### Programación Lock-Free (CAS)
En microservicios de Go (Workers), usar `sync.Mutex` para proteger una variable compartida entre 10,000 Goroutines genera Contención (Contention). Las Goroutines se duermen y el procesador pierde tiempo haciendo Context Switches.
El nivel ITMO/Warsaw reemplaza los Mutex por operaciones atómicas nativas de la CPU (Compare-And-Swap - CAS).

```go
import "sync/atomic"

// Suma concurrente SIN Lock (Lock-Free)
func AddCounter(ptr *uint64, delta uint64) {
    for {
        old := atomic.LoadUint64(ptr)
        newVal := old + delta
        if atomic.CompareAndSwapUint64(ptr, old, newVal) {
            break // ¡Éxito!
        }
        // Si otra goroutine modificó el valor en medio, el bucle repite sin dormir.
    }
}
```
Esto se llama **Spinning** o Lock-Free programming. Es extremadamente rápido pero peligroso si la contención es altísima (quemará la CPU en el bucle for infinito).

---

## 5. ⚠️ Runbook SRE Corporativo: Falsas Comparticiones (False Sharing)

**Incidente**: Implementaste un algoritmo Lock-Free usando un array `Counters[8]` donde 8 Goroutines diferentes suman puntos. Cada Goroutine usa un índice distinto (Goroutine 0 usa `Counters[0]`, Goroutine 1 usa `Counters[1]`). No hay colisión lógica. Sin embargo, **el programa es un 40% más lento** que si usarás un solo hilo.

**Causa Raíz SRE (Arquitectura de Hardware)**:
Los procesadores (L1 Cache) leen la RAM en bloques de 64 bytes (Cache Lines).
`Counters[0]` y `Counters[1]` (que son int64, 8 bytes cada uno) viven pegados en la RAM y caen **dentro de la misma línea de caché de 64 bytes**.
Cuando la CPU Core 1 actualiza `Counters[0]`, el protocolo de coherencia de caché del hardware (MESI Protocol) declara toda la línea de 64 bytes como "Sucia" (Dirty).
Inmediatamente, la CPU Core 2, que solo quería actualizar `Counters[1]`, se ve obligada matemáticamente a esperar a que la CPU 1 sincronice la RAM, aunque las variables lógicas sean distintas. Esto se llama **False Sharing**.

**Remediación ITMO/Peking (Memory Padding)**:
Para arreglarlo, debes forzar a que cada contador viva en una línea de caché distinta rellenando la memoria con "basura" (Padding).

```go
type PaddedCounter struct {
    Value uint64
    // Relleno de 56 bytes para forzar que el struct ocupe exactamente 64 bytes
    // (1 línea de caché completa).
    _ [7]uint64 
}
```
Al separar físicamente los datos, el False Sharing desaparece y el paralelismo recupera un $100\%$ de eficiencia lineal. El código es feo, pero es la única forma de domar los electrones del servidor.
