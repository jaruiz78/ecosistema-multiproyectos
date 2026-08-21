package com.corp.proyectosyntheticenzymebiofoundry.application.service;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import com.corp.proyectosyntheticenzymebiofoundry.domain.port.out.EnzymeRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PfasDegradationEnzymeDesignService {

    private final EnzymeRepositoryPort repositoryPort;

    public PfasDegradationEnzymeDesignService(EnzymeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public SyntheticEnzymeSequence designPfasDefluorinase(String designId, String substrate) {
        SyntheticEnzymeSequence sequence = SyntheticEnzymeSequence.create(designId, substrate);
        return repositoryPort.save(sequence);
    }
}
