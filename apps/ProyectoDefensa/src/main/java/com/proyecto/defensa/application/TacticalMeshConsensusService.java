package com.proyecto.defensa.application;

import com.proyecto.defensa.domain.TacticalNode;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de consensos de malla táctica desintermediada.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class TacticalMeshConsensusService {

    private final ReentrantLock lock = new ReentrantLock();

    public boolean verifyQuorum(List<TacticalNode> activeNodes, int minimumQuorum) {
        lock.lock();
        try {
            long validNodesCount = activeNodes.stream()
                    .filter(TacticalNode::airGappedActive)
                    .count();
            return validNodesCount >= minimumQuorum;
        } finally {
            lock.unlock();
        }
    }
}
