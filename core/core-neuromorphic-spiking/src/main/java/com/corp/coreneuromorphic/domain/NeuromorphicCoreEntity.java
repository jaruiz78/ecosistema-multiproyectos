package com.corp.coreneuromorphic.domain;

import java.io.Serializable;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record NeuromorphicCoreEntity(
        String networkId,
        int totalNeurons,
        double activeSpikeRateHz,
        boolean energyOptimal
) implements Serializable {}
