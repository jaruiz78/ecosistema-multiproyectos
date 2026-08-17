package com.corp.proyectosyntheticenzymebiofoundry.application.service;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import com.corp.proyectosyntheticenzymebiofoundry.domain.port.out.EnzymeRepositoryPort;

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
