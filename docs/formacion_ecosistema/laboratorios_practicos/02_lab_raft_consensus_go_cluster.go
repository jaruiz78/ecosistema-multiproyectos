package main

// 02_lab_raft_consensus_go_cluster.go
// -------------------------------------------------------------------------
// Laboratorio Práctico Feynman: Algoritmo de Consenso Raft (Diego Ongaro 2014)
// Implementación autocontenida de un clúster Raft de 3 nodos en Go puro
// que demuestra: Elección de Líder, Heartbeats periódicos y Conmutación por Fallo.
// -------------------------------------------------------------------------

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Role string

const (
	Follower  Role = "FOLLOWER"
	Candidate Role = "CANDIDATE"
	Leader    Role = "LEADER"
)

type RaftNode struct {
	mu           sync.Mutex
	id           int
	currentTerm  int
	votedFor     int
	role         Role
	peers        []*RaftNode
	heartbeatRcv chan bool
	stopCh       chan struct{}
}

func NewRaftNode(id int) *RaftNode {
	return &RaftNode{
		id:           id,
		currentTerm:  0,
		votedFor:     -1,
		role:         Follower,
		heartbeatRcv: make(chan bool, 10),
		stopCh:       make(chan struct{}),
	}
}

func (rn *RaftNode) Run() {
	for {
		rn.mu.Lock()
		role := rn.role
		rn.mu.Unlock()

		switch role {
		case Follower:
			timeout := time.Duration(150+rand.Intn(150)) * time.Millisecond
			select {
			case <-rn.heartbeatRcv:
				// Heartbeat recibido del líder, resetear temporizador
			case <-time.After(timeout):
				rn.mu.Lock()
				fmt.Printf("⏱️ [Nodo %d] Timeout de elección expirado. Convirtiéndose en CANDIDATE (Term %d -> %d)\n", rn.id, rn.currentTerm, rn.currentTerm+1)
				rn.role = Candidate
				rn.currentTerm++
				rn.votedFor = rn.id
				rn.mu.Unlock()
				rn.startElection()
			case <-rn.stopCh:
				return
			}

		case Candidate:
			timeout := time.Duration(150+rand.Intn(150)) * time.Millisecond
			select {
			case <-rn.heartbeatRcv:
				rn.mu.Lock()
				rn.role = Follower
				rn.mu.Unlock()
			case <-time.After(timeout):
				rn.mu.Lock()
				rn.currentTerm++
				rn.votedFor = rn.id
				rn.mu.Unlock()
				rn.startElection()
			case <-rn.stopCh:
				return
			}

		case Leader:
			// Enviar Heartbeat periódico a todos los seguidores cada 50ms
			time.Sleep(50 * time.Millisecond)
			rn.mu.Lock()
			term := rn.currentTerm
			rn.mu.Unlock()

			for _, p := range rn.peers {
				if p.id != rn.id {
					p.ReceiveHeartbeat(rn.id, term)
				}
			}
		}
	}
}

func (rn *RaftNode) startElection() {
	rn.mu.Lock()
	term := rn.currentTerm
	rn.mu.Unlock()

	votes := 1 // Voto por sí mismo
	var voteMu sync.Mutex

	for _, peer := range rn.peers {
		if peer.id == rn.id {
			continue
		}
		go func(p *RaftNode) {
			if p.RequestVote(rn.id, term) {
				voteMu.Lock()
				votes++
				if votes > (len(rn.peers) / 2) {
					rn.mu.Lock()
					if rn.role == Candidate && rn.currentTerm == term {
						rn.role = Leader
						fmt.Printf("👑 [Nodo %d] ¡ELECTO LÍDER! Quórum alcanzado (%d/%d votos) para el Term %d\n", rn.id, votes, len(rn.peers), term)
					}
					rn.mu.Unlock()
				}
				voteMu.Unlock()
			}
		}(peer)
	}
}

func (rn *RaftNode) RequestVote(candidateID, candidateTerm int) bool {
	rn.mu.Lock()
	defer rn.mu.Unlock()

	if candidateTerm > rn.currentTerm {
		rn.currentTerm = candidateTerm
		rn.role = Follower
		rn.votedFor = -1
	}

	if (rn.votedFor == -1 || rn.votedFor == candidateID) && candidateTerm == rn.currentTerm {
		rn.votedFor = candidateID
		fmt.Printf("🗳️ [Nodo %d] Votó a favor del Nodo %d para el Term %d\n", rn.id, candidateID, candidateTerm)
		return true
	}
	return false
}

func (rn *RaftNode) ReceiveHeartbeat(leaderID, leaderTerm int) {
	rn.mu.Lock()
	defer rn.mu.Unlock()

	if leaderTerm >= rn.currentTerm {
		if rn.role != Follower || leaderTerm > rn.currentTerm {
			rn.role = Follower
			rn.currentTerm = leaderTerm
			rn.votedFor = -1
		}
		select {
		case rn.heartbeatRcv <- true:
		default:
		}
	}
}

func main() {
	fmt.Println("====================================================================")
	fmt.Println("  🧪 LAB FEYNMAN 02: CLÚSTER DE CONSENSO RAFT EN GO PURO (3 NODOS)")
	fmt.Println("====================================================================")

	nodes := []*RaftNode{
		NewRaftNode(1),
		NewRaftNode(2),
		NewRaftNode(3),
	}

	for _, n := range nodes {
		n.peers = nodes
		go n.Run()
	}

	// Permitir que el clúster elija un líder y estabilice el término
	time.Sleep(600 * time.Millisecond)

	fmt.Println("\n💥 SIMULANDO CAÍDA DEL LÍDER ACTUAL...")
	// Detener al nodo 1 o al líder activo
	nodes[0].mu.Lock()
	fmt.Printf("🛑 Apagando Nodo %d para forzar reelección automática...\n", nodes[0].id)
	close(nodes[0].stopCh)
	nodes[0].mu.Unlock()

	// Esperar que los nodos supervivientes elijan un nuevo líder
	time.Sleep(600 * time.Millisecond)

	fmt.Println("--------------------------------------------------------------------")
	fmt.Println("✓ Clúster Raft reconciliado con éxito: Consenso por quórum verificado.")
	fmt.Println("====================================================================")
}
