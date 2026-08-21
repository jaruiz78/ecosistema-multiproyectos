package com.corp.coreqaoa.domain;

import java.io.Serializable;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record QaoaOptimizationResult(
        String graphId,
        int numNodes,
        int[] optimalSpinPartition,
        double groundStateEnergy,
        boolean quantumSpeedupAchieved
) implements Serializable {}
