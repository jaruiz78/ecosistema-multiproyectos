# Módulo 3.9: Arquitectura Transformers y Modelos Secuenciales (Nivel CMU / Tsinghua / MIT)

---

## 1. 🐣 Rincón Junior: Entendiendo el Contexto

Imagina la frase: *"El banco estaba roto, así que me senté en el suelo"*.
Una computadora antigua leería la palabra "banco" e imaginaría un edificio con dinero (institución financiera). Pero como humanos, leemos la palabra "roto" y "senté", y mágicamente entendemos que "banco" significa "asiento de madera".
Esa capacidad de conectar palabras lejanas y cambiar su significado basándose en el **Contexto** es lo que las redes neuronales antiguas (RNNs o LSTMs) hacían terriblemente mal, porque leían de izquierda a derecha olvidando el pasado.
En 2017, Google publicó el paper *"Attention Is All You Need"*, introduciendo la arquitectura **Transformer**. En vez de leer en orden, el Transformer lee **toda la frase de golpe** y calcula matemáticamente qué palabra debe "prestar atención" a qué otra palabra, sin importar lo lejos que estén.

---

## 2. 🔬 Fundamentos Matemáticos: El Mecanismo de Atención (Self-Attention) y GLM (Tsinghua)

El corazón de la IA Generativa (LLMs como GPT-4, Gemini) es una única ecuación matricial gigantesca. Académicos de la **Universidad de Tsinghua** (creadores de la arquitectura GLM - General Language Model) demostraron que la atención autorregresiva puede formularse como un problema de predicción de espacios en blanco bidireccional, superando las limitaciones direccionales tradicionales.

En una base de datos, escribes una consulta (Query), se compara contra las llaves (Keys) y te devuelve los valores (Values). En Transformers, las palabras de tu frase generan vectores continuos para estas 3 variables matemáticas:

$$Attention(Q, K, V) = \text{softmax}\left(\frac{Q K^T}{\sqrt{d_k}}\right) V$$

*   **Query ($Q$)**: Lo que estoy buscando. (La palabra "banco" preguntando: "¿De qué tipo de banco hablo?").
*   **Key ($K$)**: Lo que yo represento. (La palabra "senté" dice: "Yo tengo que ver con sentarse").
*   **$Q K^T$**: Producto punto tensorial. Mide la **similitud geométrica** (el ángulo en el espacio de Hilbert) entre la palabra actual y todas las demás. Si es alto, hay conexión semántica.
*   **$\sqrt{d_k}$**: Factor de escalado. Si el espacio latente tiene 4096 dimensiones (como LLaMA), el producto punto genera números tan inmensos que estropean el gradiente. Dividir por la raíz cuadrada estabiliza las matemáticas.
*   **softmax**: Una función mágica que convierte todos los resultados en porcentajes (probabilidades) que suman $1.0$. ("Banco" prestará 80% de atención a "senté", 15% a "suelo", y 5% a las demás).
*   **Value ($V$)**: El contenido real de la palabra, que es multiplicado por esos porcentajes y sumado, creando un nuevo vector super-enriquecido con el contexto completo.

---

## 3. 🚀 Arquitectura Computacional: Multi-Head Attention

El problema del Self-Attention puro es que una palabra solo puede enfocarse en un aspecto a la vez. ¿Qué pasa si "banco" necesita prestar atención a "roto" (adjetivo físico) y a "ayer" (tiempo gramatical) al mismo tiempo?

**Multi-Head Attention** resuelve esto clonando el mecanismo de atención múltiples veces en paralelo (ej. 12 cabezas). 
Cada "cabeza" tiene sus propias matrices de pesos entrenables ($W^Q, W^K, W^V$). Durante el entrenamiento (Backpropagation), las cabezas se especializan matemáticamente. Una cabeza puede aprender a ser experta en detectar verbos, otra en capturar dependencias a largo plazo, y otra en sintaxis. Al final, los resultados de todas las cabezas se concatenan, permitiendo un análisis semántico en altísima dimensión de forma paralela en la GPU ($O(1)$ secuencias).

---

## 4. 🧠 Internals Avanzados: Positional Encoding (Hackeando el Tiempo)

El mecanismo $Q K^T$ no tiene concepto del tiempo o del orden. A diferencia de las RNN, el Transformer procesa todas las palabras como un "Saco de Palabras" (Bag of Words) simultáneo. Si le pasamos "perro muerde a hombre" y "hombre muerde a perro", la salida matemática sería idéntica, lo cual es fatal.

**Positional Encoding** es un hack matemático brillante. Antes de enviar la palabra a la red neuronal, inyectamos una señal de alta frecuencia (como una marca de agua de radiofrecuencia) en el vector del embedding, utilizando funciones sinusoidales:

*   $PE_{(pos, 2i)} = \sin(pos / 10000^{2i/d_{model}})$
*   $PE_{(pos, 2i+1)} = \cos(pos / 10000^{2i/d_{model}})$

Sumamos esta vibración trigonométrica al vector original de la palabra. Las redes neuronales son lo suficientemente inteligentes como para "leer" esta interferencia de onda y deducir la distancia relativa matemática entre dos palabras dentro de la frase.

---

## 5. ⚠️ Runbook SRE Matemático: Complejidad $O(N^2)$ y RoPE (Context Window Exhaustion)

**Incidente**: Intentas usar el Transformer del Gemelo Digital para procesar series temporales largas (un millón de datos de tráfico), y las GPUs de 80GB VRAM de Google Cloud colapsan instantáneamente mostrando `CUDA_OUT_OF_MEMORY`.

**Diagnóstico Matemático (El Cuello de Botella del Cuadrado)**:
La matriz de Atención ($Q K^T$) compara cada token (palabra o instante de tiempo) con todos los demás. 
*   Para 1,000 tokens: La matriz tiene $1,000,000$ de celdas.
*   Para 100,000 tokens (un libro o un día de logs de tráfico): La matriz tiene $10,000,000,000$ (10 mil millones) de celdas por cada Capa de Atención por cada Cabeza, reventando matemáticamente cualquier tarjeta gráfica moderna en milisegundos por complejidad $O(N^2)$ en memoria.

**Solución SRE Arquitectónica**:
1.  **FlashAttention**: Implementación reescrita en C++/CUDA que calcula el Softmax mediante "Tiling" a nivel de SRAM del chip H100.
2.  **Mecanismos Sparse**: Abandonar la atención completa (densa). Usar *Sliding Window Attention* (Mistral), donde un token solo presta atención a sus 4096 adyacentes.
3.  **Rotary Positional Embeddings (RoPE)**: Cambiar seno/coseno estático por rotaciones en el plano complejo.
4.  **State Space Models (SSM) y Mamba (CMU)**: La **Carnegie Mellon University (CMU)** introdujo arquitecturas basadas en Teoría de Control (State Space Models) como **Mamba**. A diferencia de los Transformers ($O(N^2)$), Mamba condensa el historial de manera selectiva en un estado latente de tamaño fijo, logrando inferencia y entrenamiento en tiempo lineal $O(N)$ y consumo de memoria constante, permitiendo contextos de longitud infinita para el Gemelo Digital (series temporales continuas).

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

Implementar Transformer Attention en PyTorch (`torch.matmul`) de forma nativa oculta una profunda ineficiencia a nivel de hardware. La aceleración moderna de LLMs no es un logro matemático abstracto, sino una victoria de Arquitectura de Computadores (Computer Architecture) y el dominio jerárquico de la memoria en las GPUs NVIDIA.

## 6. Optimización CUDA: IO-Aware Attention (FlashAttention)

El verdadero cuello de botella de los LLMs modernos y los simuladores tensoriales no es el cálculo aritmético (FLOPs), sino el **Memory Bandwidth** (Llevar los datos desde la lenta VRAM HBM a la ultrarrápida pero minúscula SRAM L1 del Streaming Multiprocessor de la GPU).

La atención matemática estándar:
$S = QK^T \in \mathbb{R}^{N \times N}$
$P = \text{softmax}(S) \in \mathbb{R}^{N \times N}$
$O = PV \in \mathbb{R}^{N \times d}$

Requiere que materialicemos (escribamos y leamos) la inmensa matriz $S$ y la matriz $P$ en la VRAM HBM, lo que cuesta $O(N^2)$ escrituras y lecturas de memoria. 

### Algoritmo de Tiling (Fusion Kernels)

**FlashAttention** (Dao et al., 2022) demostró matemáticamente que podemos calcular la salida $O$ exacta sin instanciar *jamás* la matriz cuadrada $N \times N$ en memoria principal. Se basa en dividir los bloques (tiling) y reestructurar el operador `softmax`.

El problema original del `softmax` es que el denominador exige conocer la suma de los exponenciales de toda la fila, impidiendo procesar $Q$ y $K$ en trozos pequeños:
$\text{softmax}(x)_i = \frac{e^{x_i}}{\sum_{j=1}^N e^{x_j}}$

FlashAttention soluciona esto calculando un estadístico continuo (el máximo iterativo y la suma iterativa) de los bloques $K, V$ a medida que se procesa el bloque $Q$ correspondiente en la SRAM local.

1.  Cargar bloque de $Q, K, V$ de la HBM lenta a la SRAM rápida.
2.  Calcular $QK^T$ solo para ese bloque.
3.  Mantener en registros del hilo CUDA el escalar de máximo $m(x)$ y de suma $l(x)$ local.
4.  Corregir dinámicamente la escala del `softmax` anterior cuando entra el nuevo bloque de $K$.
5.  Multiplicar el bloque $V$ directamente en la SRAM y escribir el bloque de $O$ directamente a la HBM.

**Complejidad de I/O**: Pasa de $O(N^2 d)$ a $O(N^2 d^2 / M)$, donde $M$ es el tamaño de la SRAM. Se reduce el trasiego de memoria un orden de magnitud y la RAM usada es lineal $O(N)$.

## 7. Multi-Query Attention (MQA) y Grouped-Query Attention (GQA)

Cuando inferimos LLMs (generación de texto) en producción (Gemini, LLaMA), el tamaño del batch (cuántos usuarios concurrentes servimos) está limitado por la memoria reservada para el caché de atención KV (Key-Value Cache).

*   **Multi-Head Attention (MHA) Tradicional**: Cada una de las $H$ cabezas tiene sus propios vectores $Q, K, y V$. Para inferir, debemos almacenar en VRAM todo el histórico de $K$ y $V$ (KV Cache) para cada cabeza de forma independiente.
*   **Multi-Query Attention (MQA)**: Innovación radical de hardware donde todas las cabezas (ej. 32 cabezas de Query independientes) **comparten** exactamente 1 única cabeza de Key y Value. El rendimiento lingüístico casi no se degrada, pero el tamaño del KV Cache se reduce por un factor de 32, permitiendo meter a 32 veces más usuarios en la misma GPU de Google Cloud.
*   **Grouped-Query Attention (GQA)**: Compromiso intermedio usado en Llama-3. Si tenemos 32 cabezas $Q$, las agrupamos en 8 grupos, donde cada grupo de 4 $Q$ comparte 1 cabeza de $K$ y $V$. Resulta en la calidad analítica de MHA con la velocidad inferencial de MQA.

Este nivel de ingeniería SRE / ML es el que separa a los investigadores académicos (que suelen morir de `OOM Errors` corriendo algoritmos nativos) de la infraestructura corporativa que sirve IA generativa a millones de usuarios en tiempo real con latencias $< 50$ ms.
