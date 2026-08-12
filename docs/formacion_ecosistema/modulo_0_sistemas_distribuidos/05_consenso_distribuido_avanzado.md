# Módulo 0.5: Consenso Distribuido Avanzado (Paxos, Raft, EPaxos)

---

## 1. 🐣 Rincón Junior: El Problema de Acordar un Valor

Imagínate tres senadores romanos en distintas ciudades. Quieren acordar si atacar mañana o no. No tienen un líder (todos son iguales).
El Senador 1 manda palomas mensajeras: "Yo voto atacar".
El Senador 2: "Yo voto no atacar".
El Senador 3 se muere.
Tienen que llegar a un acuerdo mayoritario a pesar de los retrasos de las palomas, a pesar de que pueden haber sugerido ideas distintas al mismo tiempo y a pesar de la muerte de compañeros.
Este es el **Problema del Consenso**, el santo grial de las ciencias de la computación distribuida.

---

## 2. 🔬 Fundamentos Arquitectónicos: La Isla de Paxos

En 1989, el genial y excéntrico matemático Leslie Lamport escribió un *paper* alegórico describiendo un parlamento en la antigua isla griega de Paxos, donde los legisladores entraban y salían, y los mensajeros eran ineficientes. Durante años nadie lo tomó en serio por su tono humorístico, hasta que en 1998 se volvió a publicar y revolucionó el mundo corporativo.

**Paxos** es el primer algoritmo matemáticamente demostrado que logra el consenso de forma segura en un sistema asíncrono con fallos de tipo *crash-stop* (pero no bizantinos).

**El Algoritmo Básico (Single-Decree Paxos):**
Paxos opera dividiendo a los nodos en roles: **Proposers** (Proponentes), **Acceptors** (Aceptadores) y **Learners** (Aprendices).

*Fase 1: Preparación (Prepare/Promise)*
1. Un Proponente elige un número de propuesta único `N` (ej. Timestamp + ID del Nodo) y lo envía a la mayoría de los Aceptadores.
2. Si un Aceptador recibe `Prepare(N)`, y `N` es mayor que cualquier otro número que haya visto antes, promete no aceptar propuestas menores a `N` y devuelve el último valor que aceptó (si es que aceptó alguno).

*Fase 2: Aceptación (Accept/Accepted)*
3. Si el Proponente recibe respuestas de la mayoría, escoge el valor de la propuesta de mayor número que le devolvieron (o su propio valor si nadie devolvió nada).
4. El Proponente envía el mensaje `Accept(N, Valor)` a los Aceptadores.
5. Los Aceptadores aceptan el valor *solo si* no han prometido responder a un `Prepare` con un número mayor a `N`.

*Fase 3: Aprendizaje*
6. Una vez aceptado por la mayoría, se envía a los Learners (quienes ejecutan la acción final).

**La Genialidad**: Múltiples nodos pueden intentar ser Proponentes a la vez (Dueling Proposers). Sus `N` chocarán. Solo el que tenga el `N` más alto prevalecerá forzando a los demás a retroceder. 

---

## 3. 🧠 Internals Avanzados: Multi-Paxos y Raft

Single-Decree Paxos es pesado: requiere 2 viajes de ida y vuelta (Round-Trips - RTT) por la red solo para acordar un mísero dato.

### Multi-Paxos
Para sistemas reales (como Google Chubby, Spanner), usamos Multi-Paxos.
1. Se realiza la Fase 1 una sola vez, eligiendo matemáticamente a un "Proponente Líder" temporal.
2. Una vez que este Líder está establecido, para las siguientes 10.000 operaciones, solo se usa la Fase 2 (1 RTT).
3. Es ultra rápido, pero extremadamente difícil de implementar sin bugs sutiles, porque el *paper* original deja fuera muchos detalles de ingeniería.

### Raft (Stanford, 2014)
Raft nació por una frustración colectiva en la industria: "Paxos es incomprensible y demasiado difícil de implementar correctamente".
Raft logra *exactamente* el mismo nivel de seguridad matemática que Multi-Paxos, pero descompone el problema obligatoriamente en tres cajas separadas:
1. Elección de Líder estricta (Jitter Timeout).
2. Replicación de Log obligada y secuencial.
3. Seguridad y consistencia (Commitment).

*Diferencia Clave*: En Paxos, cualquier nodo puede proponer un valor (es simétrico y caótico). En Raft, la simetría está prohibida. Solo el Líder propone. Si el Líder muere, se para el mundo hasta elegir otro Líder (Asimetría obligada).

---

## 4. 🛑 [DEEP-DIVE] Teoría y Práctica Post-Doc / Industry Fellow

El problema tanto de Multi-Paxos como de Raft es el **Cuello de Botella del Líder**.
Para centros de datos geodistribuidos a nivel mundial (ej. un nodo en Tokyo, uno en NY, uno en Londres), obligar a que todo el tráfico del mundo vaya al Líder de NY para ser ordenado añade latencias de hasta 200ms por cada operación de escritura.

### Egalitarian Paxos (EPaxos - Moraru, Andersen, Kaminsky, 2013)

EPaxos elimina el Líder de raíz (Leaderless Consensus) y logra comprometer datos (Commits) en tan solo **1 RTT** (un viaje de ida y vuelta de luz), el mínimo teórico de la física y la teoría de la información, rompiendo los límites de Raft.

**Fundamento Matemático de EPaxos:**
En lugar de forzar un orden total global para todos los comandos (Líder dictatorial), EPaxos crea un **Orden Parcial de Dependencias (Dependency Graph)** calculando la *Conmutatividad* de los comandos en tiempo real.

1. **Simetría Total**: Cualquier nodo puede recibir una petición HTTP de un cliente y actuar como "Proponente Comando" (Command Leader) de esa única petición.
2. **Chequeo de Conflicto en RAM (Interference)**: Si el cliente de Tokio modifica la fila `User:123` y el cliente de Londres modifica la fila `Store:40`, el algoritmo calcula que estos comandos conmutan (son disjuntos). **No hay conflicto**.
3. **El Fast-Path (1 RTT)**: Como no hay conflicto, el nodo de Tokio propone su comando a sus nodos más cercanos. Si recibe respuesta del *"Fast Quorum"*, ¡el comando se considera comprometido (`Committed`) sin haber tenido que ir a Nueva York!
4. **El Slow-Path (2 RTT)**: Si dos clientes modifican `User:123` al mismo tiempo desde continentes distintos, los nodos se dan cuenta del conflicto al cruzar dependencias. Automáticamente ejecutan un protocolo clásico estilo Paxos de 2 rondas (Slow-Path) para romper el empate, y luego se consolida.

### Análisis Asintótico y de Latencia
Si la probabilidad de que las transacciones choquen entre usuarios concurrentes en la Base de Datos es pequeña (lo cual es normal, la gente compra cosas distintas), EPaxos se ejecuta en la ruta de "Fast-Path" el 95% de las veces.
*   **Raft (con Líder en NY):** Cliente Tokio $\rightarrow$ NY (100ms) $\rightarrow$ NY Replicando a Londres/Tokio (100ms) $\rightarrow$ NY respondiendo a Cliente Tokio (100ms). = **300ms de Latencia**.
*   **EPaxos:** Cliente Tokio $\rightarrow$ Nodo Tokio (1ms). Nodo Tokio habla con el Nodo Singapur (Fast-Quorum) (30ms). = **31ms de Latencia**. Una reducción drástica del orden de magnitud (10x).

> [!TIP]
> **Takeaway Arquitectónico**
> Mientras Raft domina en sistemas de un solo Datacenter de baja latencia (etcd, Kubernetes Control Plane), la próxima generación de arquitecturas Cloud "Multi-Región Planetaria" (como los componentes internos experimentales de CockroachDB y Spanner) investigan arquitecturas Multi-Paxos Híbridas o Leaderless similares a EPaxos para mitigar la velocidad de la luz (latencia de fibra óptica intercontinental).
