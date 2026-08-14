# Módulo 0.4: Topologías de Red en Datacenters (Clos y Fat-Tree)

---

## 1. 🐣 Rincón Junior: El Embudo de Internet

Si en tu casa tienes 4 ordenadores conectados a un router que da WiFi, los 4 pueden hablar entre sí rápido. Pero si esos 4 quieren bajarse una película a la vez, el cable de fibra óptica que va a la calle (el enlace ascendente) hace de "embudo". Tu conexión a internet se colapsa.
En un Datacenter gigante de Google, tienes 100,000 servidores conectados. Si el Servidor A quiere mandar fotos al Servidor B, los datos tienen que subir por cables físicos, cruzar switches masivos (conmutadores) y bajar.
Si conectásemos los 100,000 servidores como en un árbol tradicional, el router central de arriba del todo necesitaría soportar Terabits por segundo de cables, algo que la física y el electromagnetismo actual no pueden fabricar sin derretirse. Necesitamos topologías geométricas inteligentes.

---

## 2. 🔬 Fundamentos Arquitectónicos: Oversubscription y Bisection Bandwidth

Para evaluar qué tan buena es la red matemática de un datacenter, usamos dos métricas clave:

1.  **Oversubscription (Sobresuscripción)**: Es el ratio entre el ancho de banda que pueden generar los servidores de abajo frente a lo que pueden tragar los cables que suben. Un ratio 1:1 es la utopía (no hay cuellos de botella). Un ratio 10:1 significa que si los 10 servidores envían datos al máximo a la vez hacia afuera, la red descarta el 90% de los paquetes (Congestión masiva).
2.  **Bisection Bandwidth (Ancho de Banda de Bisección)**: El test definitivo. Imagina que pasas una espada matemática que corta el datacenter exactamente por la mitad, separando 50,000 servidores a la izquierda y 50,000 a la derecha. ¿Cuál es la suma máxima de ancho de banda de todos los cables que has cortado con la espada? Representa la velocidad máxima a la que la mitad A puede comunicarse con la mitad B sin colapsar.

---

## 3. 🚀 Topologías Clásicas: El Árbol (Tree) y sus Defectos

La arquitectura clásica de Cisco de 3 capas:
1.  **Access (Acceso o ToR - Top of Rack)**: Un switch encima de cada armario (Rack) de 40 servidores.
2.  **Aggregation (Agregación)**: Conecta varios armarios de una fila.
3.  **Core (Núcleo)**: Super-routers gigantes y costosísimos (millones de dólares) que conectan todo el edificio.

**El Problema Matemático (Escalabilidad Vertical)**: A medida que el tráfico horizontal (Este-Oeste, de un servidor interno a otro servidor interno) crece masivamente debido a los microservicios, el tráfico sube hacia el Core y lo ahoga. Como no existe tecnología para hacer el router Core más grande y potente (límites de silicio), el Árbol colapsa.

---

## 4. 🧠 Internals Avanzados: Redes Clos y Fat-Tree (Google Jupiter)

Para solucionar el colapso del Árbol de 3 capas, las mega-corporaciones (Meta, Google, Amazon) miraron a 1953. Charles Clos había diseñado redes matemáticas sin bloqueos para sistemas telefónicos.

**La Arquitectura Fat-Tree (Árbol Gordo)**:
En lugar de tener 2 routers Core gigantes y carísimos arriba del todo, ponemos cientos de switches pequeños, baratos (Commodity Hardware) interconectados en una matriz geométrica perfecta.

1.  A medida que subes por el árbol, en lugar de poner enlaces más gordos (cables más anchos), pones **más cantidad** de enlaces paralelos idénticos.
2.  La topología Fat-Tree garantiza un oversubscription de **1:1** en todo el centro de datos. Cualquier servidor puede hablar con cualquier otro servidor al máximo de velocidad de su tarjeta de red (ej. 10 Gbps) sin que exista un cuello de botella central, porque hay múltiples caminos geométricos alternativos.

**Spine-Leaf (La implementación moderna de Clos)**:
En los datacenters de 2 niveles actuales:
*   **Hojas (Leaves)**: Los servidores se conectan a switches Hoja.
*   **Espina (Spine)**: Cada switch Hoja está conectado físicamente a **TODOS Y CADA UNO** de los switches Espina (creando un entramado de cables monstruoso pero ordenado).
*   *Matemática*: La distancia entre dos servidores cualesquiera del mundo es **siempre exactamente 3 saltos** (Servidor A $\rightarrow$ Leaf 1 $\rightarrow$ Spine X $\rightarrow$ Leaf 2 $\rightarrow$ Servidor B). Latencia predecible y constante, vital para transacciones de alta frecuencia.

---

## 5. ⚠️ Runbook SRE: ECMP y Blackholing de Paquetes

**Incidente**: En una arquitectura Spine-Leaf, un enlace de fibra óptica entre el Leaf 1 y el Spine 4 se ensucia de polvo y empieza a perder el 50% de los paquetes sin llegar a apagarse físicamente (Gray Failure). Los usuarios aleatoriamente se quejan de que el Gemelo Digital va a tirones, pero los sistemas de monitorización (Ping) dicen que la red está viva.

**Diagnóstico SRE (ECMP - Equal-Cost Multi-Path routing)**:
Como hay decenas de caminos exactos (Spines) entre el servidor A y B, los switches Leaf usan el algoritmo matemático ECMP para repartir el tráfico (ej. el Paquete 1 va por Spine 1, el Paquete 2 por Spine 4). ECMP usa un Hash de la IP y el Puerto para "pegar" un flujo de TCP siempre al mismo camino para evitar desorden (Out-of-Order).
Si el flujo de video en vivo del usuario X cae en la ranura matemática que la función Hash envía por el Spine 4 averiado, el vídeo morirá, mientras que el usuario Y, yendo por el Spine 3, lo verá en 4K.

**Solución SRE**:
Las redes modernas de Data Centers deben usar algoritmos de recolección de telemetría activa (ej. In-band Network Telemetry - INT) o sondas de software en los hosts que detecten la latencia anómala en micro-flujos TCP. Cuando el software SRE detecta un "Agujero Negro" parcial, ejecuta un comando para reescribir las tablas ECMP y sacar matemáticamente el enlace `Leaf1-Spine4` de la ecuación, mitigando el error de hardware sin que un humano baje al centro de datos con un soplador de aire comprimido.

---
---

# 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

Expandimos el análisis matemático de las topologías de Datacenter evaluando los límites teóricos del Ancho de Banda de Bisección y el diseño del Algoritmo de Enrutamiento en estructuras k-ary Fat-Tree.

## 6. Matemática del k-ary Fat-Tree (Al-Fares, Loukissas, Vahdat - 2008)

La topología Fat-Tree se construye utilizando switches "commodity" (estándar, baratos, por ejemplo, switches con $k$ puertos Gigabit Ethernet).
Toda la arquitectura depende del parámetro $k$ (número de puertos por switch).

**Ecuaciones de Construcción:**
- Hay **3 capas**: Edge (Borde), Aggregation (Agregación) y Core (Núcleo).
- Se divide la capa de Edge y Aggregation en **Pods**. Un pod contiene $(k/2)$ switches Edge y $(k/2)$ switches Aggregation. Hay un total de $k$ pods.
- Cada switch Edge se conecta a $(k/2)$ servidores.
- Total de servidores soportados: $N = k^3 / 4$.
- Total de switches necesarios: $(5k^2) / 4$.
- Total de switches en el Core: $(k/2)^2$.

**Ejemplo Práctico:** Si usamos switches baratos de 48 puertos ($k = 48$):
- Servidores soportados: `$48`^3 / 4 = 110,592 / 4 = 27,648$ servidores.
- Switches en el Core: $(24)^2 = 576$ switches.

**Teorema del Bisection Bandwidth (Ancho de Banda de Bisección):**
En un k-ary Fat-Tree perfecto, si cada enlace tiene un ancho de banda $C$ (ej. 10 Gbps), el Bisection Bandwidth de la red entera es:
$BB = \frac{N \times C}{2}$
Si tenemos 27,648 servidores a 10 Gbps, la red ofrece `$138`,240 \text{ Gbps}$ (138 Tbps) de cruce puro, siendo una red *Non-Blocking* (Oversubscription 1:1 real).

## 7. Desafíos de Enrutamiento y El Fracaso del Spanning Tree (STP)

Históricamente, Ethernet a Nivel 2 (L2) usaba el protocolo **Spanning Tree Protocol (STP)** para evitar bucles. 
*El problema de STP:* STP soluciona los bucles apagando matemáticamente los enlaces redundantes. Convierte un grafo altamente interconectado (el Fat-Tree) en un miserable árbol simple, apagando el 90% de los cables por los que acabas de pagar millones.

**La Solución Post-Doc: Enrutamiento L3 en todas partes (BGP / OSPF / ECMP)**
Para aprovechar un Fat-Tree, las corporaciones eliminan Ethernet L2 fuera del Rack (ToR). 
1. A cada servidor y a cada switch se le asigna una subred IP única (IP Clos).
2. Se utiliza el protocolo BGP (Border Gateway Protocol) de forma interna (iBGP/eBGP). Cada switch es un "país" autónomo.
3. Al existir múltiples caminos con el mismo coste (misma métrica BGP), se activa **ECMP**.

### Hashing Avanzado en ECMP y Colisiones (Flow Elephant)
ECMP usa una función hash (ej. 5-tuple: IP origen, IP destino, Protocolo, Puerto origen, Puerto destino) para elegir el Spine de subida.
`camino = Hash(5-tuple) % cantidad_de_spines`

**El Problema de los "Elephant Flows":**
¿Qué pasa si tienes 100 flujos pequeños (Ratones) y 1 flujo masivo (Elefante - ej. un backup de Hadoop de 500GB)?
Si por mala suerte matemática, la función Hash asigna el Flujo Elefante al mismo enlace que 50 flujos pequeños, ese enlace se saturará localmente (Microbursts) y perderá paquetes, mientras que los enlaces paralelos estarán vacíos al 1%.
*Solución Avanzada (Google Jupiter / Flowlet Routing):* En lugar de anclar una conexión TCP entera a un solo cable (flow), la red divide TCP en "Flowlets" (ráfagas de paquetes separados por milisegundos de silencio) y reasigna enlaces dinámicamente según la congestión instantánea.
