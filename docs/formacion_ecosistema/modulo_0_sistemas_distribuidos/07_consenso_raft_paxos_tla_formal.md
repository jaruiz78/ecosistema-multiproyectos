# 🌐 Cátedra de Sistemas Distribuidos, Consenso & TLA+ Formal (Nivel MIT 6.5840 / UC Berkeley)
## *Facultad II: Consenso Raft, Paxos, Teorema FLP, PACELC y Verificación Formal en TLA+*

---

### 🏛️ 1. Fundamentos Matemáticos y Teoremas Canónicos

#### Relojes Lógicos de Lamport y Relación Causal Happened-Before (1978)
Leslie Lamport formalizó el ordenamiento parcial de eventos en sistemas distribuidos asíncronos mediante la relación causal **happened-before** (\(a \to b\)):
1. Si \(a\) y \(b\) ocurren en el mismo proceso y \(a\) precede a \(b\), entonces \(a \to b\).
2. Si \(a\) es el envío de un mensaje y \(b\) es la recepción del mismo mensaje, entonces \(a \to b\).
3. Transitividad: \(a \to b \land b \to c \implies a \to c\).

Los **relojes** lógicos asignan un escalar monótono \(C(e)\) a cada evento garantizando la condición de **relojes** de Lamport:
$$a \to b \implies C(a) < C(b)$$

Para lograr **consenso** total y resolver la concurrencia distribuida en ausencia de tiempo físico sincronizado, algoritmos como **Raft**, **Paxos** y **tla+** extienden este orden causal hacia máquinas de estados replicadas (*Replicated State Machines*).

#### Seguridad (*Safety*) y Vivacidad (*Liveness*) en Consenso
En sistemas distribuidos asíncronos con fallos por caída de nodos (*crash-recovery*), el problema del **consenso** consiste en lograr que un conjunto de procesos acuerde un único valor garantizando:
- **Seguridad (*Safety*)**: Ningún par de nodos correctos decide valores distintos.
- **Vivacidad (*Liveness*)**: Todo nodo correcto eventualmente decide un valor (*Termination*).

#### El Teorema de Imposibilidad FLP (Fischer, Lynch, Paterson 1985)
En un sistema asíncrono, **ningún algoritmo determinista de consenso puede garantizar vivacidad si existe la posibilidad de que un único nodo falle por parada (*crash failure*)**.
Para sortear el teorema FLP, algoritmos como **Raft** y **Multi-Paxos** introducen suposiciones de sincronía débil con temporizadores aleatorizados.

#### Teorema PACELC (Daniel Abadi 2012)
Ampliación del Teorema CAP: En caso de Partición (\(P\)), elegir entre Disponibilidad (\(A\)) o Consistencia (\(C\)); si no hay partición (\(E\)), elegir entre Latencia (\(L\)) o Consistencia (\(C\)):
$$\text{PACELC} = (\text{If } P \to \{A \lor C\}) \land (\text{Else } \to \{L \lor C\})$$

---

### 🗳️ 2. Algoritmo Raft: Descomposición Formal en 3 Subproblemas

Raft divide el consenso en tres módulos formalmente independientes:

1. **Elección de Líder (*Leader Election*)**:
   - Los nodos transicionan entre tres estados: `Follower`, `Candidate`, `Leader`.
   - Si un seguidor no recibe *heartbeats* en un intervalo aleatorio \(T_{\text{election}} \in [150\text{ ms}, 300\text{ ms}]\), incrementa su término (\(\text{term} \leftarrow \text{term} + 1\)) y solicita votos (`RequestVote`).
   - Requiere mayoría estricta de quórum:
     $$Q = \left\lfloor \frac{n}{2} \right\rfloor + 1$$

2. **Replicación de Log (*Log Replication*)**:
   - El líder recibe comandos de clientes, los añade a su log local y emite RPCs `AppendEntries` en paralelo.
   - Un registro de log se considera comprometido (*committed*) cuando está replicado en la mayoría \(Q\).

3. **Invariante de Seguridad (*Election Safety*)**:
   - Si un líder ha comprometido una entrada para un término dado, esa entrada estará presente en los logs de los líderes de todos los términos superiores:
     $$\forall \text{leader } L_{\text{term}'}, \quad \text{term}' > \text{term} \implies \text{committed}(\text{entry}) \in \text{log}(L_{\text{term}'})$$

---

### 🔬 3. Especificación Formal en TLA+ (PlusCal)

```tla
-------------------------------- MODULE RaftConsensus --------------------------------
EXTENDS Naturals, Sequences, FiniteSets

CONSTANTS Server, Value
VARIABLES currentTerm, state, log, committedIndex

StateInvariants ==
    \A s1, s2 \in Server:
        (state[s1] = "Leader" /\ state[s2] = "Leader" /\ currentTerm[s1] = currentTerm[s2]) => s1 = s2

LogMatchingInvariant ==
    \A s1, s2 \in Server, i \in 1..Len(log[s1]):
        (i <= Len(log[s2]) /\ log[s1][i].term = log[s2][i].term) =>
            SubSeq(log[s1], 1, i) = SubSeq(log[s2], 1, i)
=====================================================================================
```

---

### 💡 4. Analogía Feynman (Isomorfismo Institucional)

* **Metáfora del Consilium Romano:**
  Imagina un senado donde los senadores están en villas separadas y solo se comunican por mensajeros a caballo que pueden perderse o retrasarse (red asíncrona). Para aprobar una ley (hacer *commit* de una transacción), el Cónsul (Líder) debe enviar la propuesta a todos y recibir el sello de cera de la mayoría absoluta de los senadores ($Q$). Si el Cónsul deja de enviar mensajeros, el primer senador cuyo reloj de arena se agota se proclama candidato y pide los votos. La regla de oro es que ningún senador vota a un candidato cuyo pergamino tenga menos leyes registradas que el suyo propio (*Election Restriction*).

---

### 📚 Bibliografía de Cátedra
- Fischer, M. J., Lynch, N. A., & Paterson, M. S. (1985). *Impossibility of distributed consensus with one faulty process*. JACM.
- Ongaro, D., & Ousterhout, J. (2014). *In Search of an Understandable Consensus Algorithm (Raft)*. USENIX ATC.
- Lamport, L. (1978). *Time, clocks, and the ordering of events in a distributed system*. CACM.
- Lamport, L. (2002). *Specifying Systems: The TLA+ Language and Tools for Hardware and Software Engineers*.
