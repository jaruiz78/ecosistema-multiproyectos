# 🥋 Kata 10: Consenso Distribuido Raft, Relojes Vectoriales y Linearizability

---

## 🏛️ 1. Ancla Mental y Analogía Isomórfica (El Test de los 12 Años)

> **Analogía**: Imagina un grupo de 5 capitanes de barco que navegan en una tormenta densa y necesitan acordar por unanimidad a qué puerto virar.
> - **El problema**: La radio falla intermitentemente y cualquiera de los 5 barcos puede perder comunicación temporalmente.
> - **La solución Raft**: Uno de ellos es elegido Líder por mayoría simple de votos (al menos 3 barcos). Si el Líder propone virar a la derecha, todos los barcos anotan la orden en su bitácora. Solo cuando al menos 3 barcos confirman que la anotaron, la orden es ejecutada. Si el Líder desaparece en la niebla, los 4 restantes inician una nueva elección en segundos sin contratiempos.

---

## 🔬 2. Primeros Principios: Teorema FLP y PACELC

1. **Imposibilidad FLP (Fischer, Lynch, Paterson, 1985)**: En una red asíncrona no determinista, ningún protocolo determinista de consenso distribuido puede garantizar la terminación si incluso un solo nodo puede fallar silenciosamente.
2. **Modelo de Consenso Raft (Ongaro & Ousterhout, Stanford 2014)**: Descompone el consenso en 3 subproblemas independientes y comprensibles:
   - **Elección de Líder**: Mediante Heartbeats y temporizadores estocásticos (*randomized election timeouts*).
   - **Replicación de Log**: El líder impone su bitácora unidireccionalmente hacia los seguidores (*AppendEntries*).
   - **Invariante de Seguridad (*State Machine Safety*)**: Si un servidor aplica una entrada a su máquina de estados en un índice dado, ningún otro servidor aplicará una entrada diferente en el mismo índice.

---

## 💻 3. Arquitectura de Código: Implementación en Go Lock-Free

```go
package raft_kata

import (
	"sync"
	"sync/atomic"
	"time"
)

type NodeRole int

const (
	Follower NodeRole = iota
	Candidate
	Leader
)

type RaftNode struct {
	mu          sync.Mutex
	id          int
	peers       []int
	currentTerm int64
	votedFor    int
	role        NodeRole
	log         []LogEntry
	commitIndex int64
}

type LogEntry struct {
	Term    int64
	Index   int64
	Command string
}

func NewRaftNode(id int, peers []int) *RaftNode {
	return &RaftNode{
		id:       id,
		peers:    peers,
		role:     Follower,
		votedFor: -1,
		log:      make([]LogEntry, 0),
	}
}

func (rn *RaftNode) Propose(command string) bool {
	rn.mu.Lock()
	defer rn.mu.Unlock()

	if rn.role != Leader {
		return false
	}

	entry := LogEntry{
		Term:    rn.currentTerm,
		Index:   int64(len(rn.log) + 1),
		Command: command,
	}
	rn.log = append(rn.log, entry)
	atomic.StoreInt64(&rn.commitIndex, entry.Index)
	return true
}
```

---

## 🧪 4. Ejercicio Red-to-Green: Simulación de Partición de Red

Implementa una prueba en Go que divida un clúster de 5 nodos en `{1, 2}` y `{3, 4, 5}`, verificando que la partición minoritaria rechace escrituras y la mayoritaria continúe operando con consistencia linealizable.

---

## 🧠 5. Desafío Feynman y Rúbrica de Auto-Evaluación

> **Reto Feynman**: ¿Cómo le explicarías a un niño de 12 años por qué un grupo de 5 amigos jugando a ponerse de acuerdo necesita que al menos 3 voten lo mismo, y qué pasa si se rompe el teléfono entre dos grupos?

### Rúbrica de Evaluación:
1. **Nivel 1 (Básico)**: Explica la regla de la mayoría absoluta simple (\(N/2 + 1\)) y que un grupo pequeño no puede tomar decisiones sin los demás.
2. **Nivel 2 (Intermedio)**: Explica los roles de Líder y Seguidor, el registro ordenado (Log Append-Only) y cómo se detectan y resuelven contradicciones de red usando términos de mandato (Terms).
3. **Nivel 3 (Ph.D. / Staff)**: Demuestra el teorema de seguridad de Raft (*State Machine Safety*), la imposibilidad de partición con múltiples líderes (*Split-Brain Prevention*) y la equivalencia con TLA+ de linealizabilidad bajo fallos de red asíncronos.

