# Módulo 6.2: Sistemas Distribuidos, Teorema CAP y PACELC (Nivel Berkeley)

---

## 1. 🐣 Rincón Junior: El Dilema de los Tres Cuadernos

Imagina que tú y dos amigos anotáis reservas de un restaurante en tres cuadernos distintos. Si alguien llama por teléfono para reservar:
*   Opción A: Anotas en tu cuaderno, le cuelgas al cliente diciendo "Reserva Confirmada", y luego llamas a tus dos amigos para que copien la reserva. **Es Rápido (Disponible)**, pero si tus amigos reciben una llamada justo en ese milisegundo, podrían reservar la misma mesa dos veces (**Inconsistente**).
*   Opción B: Cuando te llaman, pones al cliente en espera. Llamas a tus dos amigos, les obligas a anotar en sus cuadernos, verificas que lo han hecho, y solo entonces le dices al cliente "Reserva Confirmada". **Es Consistente (Seguro)**, pero es lentísimo y si uno de tus amigos se fue al baño y no contesta el teléfono, el sistema entero se queda bloqueado esperando (**No Disponible**).

Esta es la tragedia fundamental de cualquier base de datos (PostgreSQL, Cassandra, Mongo) cuando corre en más de un servidor. No puedes tenerlo todo.

---

## 2. 🔬 Fundamentos Matemáticos: La Prueba de Gilbert y Lynch (2002)

Eric Brewer postuló el Teorema CAP como conjetura en 2000. En 2002, Nancy Lynch y Seth Gilbert (MIT) publicaron la demostración matemática rigurosa: *"Brewer's conjecture and the feasibility of consistent, available, partition-tolerant web services"*.

El teorema modela un sistema distribuido asíncrono con nodos $G = \{n_1, n_2\}$.
1.  **Consistency (Linearizability)**: Existe un orden total de operaciones. Toda lectura posterior a una escritura exitosa debe retornar el valor escrito.
2.  **Availability**: Todo nodo que no haya fallado debe retornar una respuesta a una petición en tiempo finito, independientemente del estado de la red.
3.  **Partition Tolerance**: La red sufre pérdida de mensajes arbitraria (los nodos $n_1$ y $n_2$ pierden conexión).

**La Demostración (Prueba por Contradicción)**:
Supongamos un sistema que garantiza C, A, y P simultáneamente.
1. Ocurre una partición $P$ de red entre $n_1$ y $n_2$. Todos los mensajes entre ellos se dropean.
2. Un cliente escribe el valor $v_1$ en $n_1$. Dado que el sistema garantiza Disponibilidad ($A$), $n_1$ debe procesar la petición y retornar "OK" sin comunicarse con $n_2$.
3. Un cliente lee el valor desde $n_2$. Dado que el sistema garantiza Disponibilidad ($A$), $n_2$ debe responder en tiempo finito.
4. Como $n_2$ no pudo recibir el mensaje de $n_1$ debido a la partición $P$, $n_2$ retorna un valor $v_0$ (viejo).
5. Se ha violado la Consistencia ($C$) ($v_0 \neq v_1$).
$\therefore \text{C, A y P no pueden existir simultáneamente.}$ Q.E.D.

**Conclusión Práctica**: Como ingeniero, no puedes controlar que los cables de red se corten ($P$ es inherente a la física). Tu única elección arquitectónica en el momento del fallo es: **¿Me apago (CP) o devuelvo datos corruptos (AP)?**.

---

## 3. 🚀 Arquitectura Práctica: El Teorema PACELC (La Vida Diaria)

El Teorema CAP solo describe el comportamiento durante catástrofes (corte de red). ¿Qué pasa el 99.9% del tiempo cuando la red funciona perfectamente?
Daniel Abadi expandió CAP creando la taxonomía **PACELC**:
*If Partition: A or C. Else: L or C.*

"Si hay una **P**artición, eliges **A** o **C**. **E**n el resto del tiempo (estado nominal), debes elegir entre **L**atencia o **C**onsistencia".

*   **Latencia (L)**: Para responder en $<1ms$ (Ej. carrito de compras), escribes en el disco local y retornas "OK" (Write-Ahead). La replicación al resto de nodos ocurre en *background*. (Se sacrifica $C$ a favor de $L$).
*   **Consistencia (C)**: Para transacciones bancarias, el "OK" debe esperar el round-trip (RTT) físico de la red para garantizar que los otros nodos también guardaron el dato (Quórum síncrono). (Se sacrifica $L$ a favor de $C$).

**Ejemplos de Arquitectura**:
*   **Cassandra (AP/EL)**: Diseñado en Amazon (Dynamo) para alta disponibilidad. Ante un corte (P), sigue activo (A). En estado normal (E), escribe ultrarrápido (L), usando Eventual Consistency.
*   **Google Spanner / CockroachDB (CP/EC)**: Bases de datos NewSQL. Ante un corte (P), pierden disponibilidad para no romper las cuentas matemáticas (C). En estado normal (E), incurren en latencia de consenso de Paxos/Raft (C).

---

## 4. 🧠 Internals Avanzados: Matemáticas del Quórum (Raft)

Para que los sistemas **CP** (Consistentes) puedan tomar decisiones sin bloqueos infinitos ante fallos parciales, usan el principio del Quórum de Consenso Mayoritario (Algoritmo Raft, base de etcd en Kubernetes).

Sea $N$ el número total de nodos. El Quórum $Q$ requerido para validar una escritura se define matemáticamente por el Principio de Pigeonhole (Palomar) para garantizar intersección:
$$ Q = \lfloor \frac{N}{2} \rfloor + 1 $$

*   Si $N=3$, $Q=2$. Tolera $1$ fallo.
*   Si $N=5$, $Q=3$. Tolera $2$ fallos.
*   *Nota*: Nunca se despliegan $N$ pares ($N=4$) porque el Quórum sería $3$, tolerando igualmente solo $1$ fallo, pero añadiendo más latencia de red inútilmente.

**Resolución del Split-Brain**:
Si un clúster de $N=5$ sufre un corte de fibra óptica dividiéndose en particiones $P_A$ (2 nodos) y $P_B$ (3 nodos):
*   $P_A$ exige un Quórum de $3$ pero solo tiene $2$. Sus escrituras son bloqueadas físicamente. (Sacrifica A, asegura C).
*   $P_B$ tiene $3$ nodos, logra el Quórum. Sigue operando con normalidad.
Jamás pueden existir dos mayorías simultáneas (Dilema bizantino resuelto).

---

## 5. ⚠️ Runbook SRE Arquitectónico: Double-Spending Financiero

**Incidente SRE**: Se usó Cassandra (AP/EL) para gestionar los saldos bancarios de las Wallets de los usuarios del Gemelo Digital.
Durante un problema BGP en AWS, el usuario retiró 100€ del Nodo de París, y simultáneamente un atacante script retiró 100€ del Nodo de Frankfurt (Split-Brain intencional). Ambos nodos, al estar desconectados, autorizaron la operación (Priorizando Disponibilidad $A$). Al reconectarse la red, los nodos fusionaron los deltas (LWW-Element-Set CRDT), el usuario gastó 200€, pero solo tenía 100€ de saldo real (Double Spending).

**Diagnóstico SRE/Arquitectónico**:
El sistema financiero requiere estrictamente Linearizability (CP/EC). No se puede usar Consistencia Eventual ni Resolución de Conflictos asíncrona para transacciones atómicas de saldo.
**Solución Arquitectónica Inmediata**: Migrar el microservicio de `Wallet` de Cassandra a PostgreSQL (Monolítico local) o Google Spanner (CP Distribuido). Restringir Cassandra/MongoDB (AP) únicamente a telemetría IoT, logs y carritos de compra donde un read-repair posterior es matemáticamente inocuo.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Sistemas Distribuidos, Teorema CAP y PACELC (Nivel Berkeley)** a un estudiante de secundaria, **sin usar las palabras:** "Sistemas", "Distribuidos,", "Teorema" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
