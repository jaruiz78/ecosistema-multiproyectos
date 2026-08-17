package com.corp.proyectostratosphericaerosolgeoengineering.application.service;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out.AerosolPlumeRepositoryPort;

public class RadiativeForcingGeoengineeringService {

    private final AerosolPlumeRepositoryPort repositoryPort;

    public RadiativeForcingGeoengineeringService(AerosolPlumeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public StratosphericAerosolPlume planStratosphericInjection(String injectionId, double altitudeKm, double massMt) {
        StratosphericAerosolPlume plume = StratosphericAerosolPlume.create(injectionId, altitudeKm, massMt);
        return repositoryPort.save(plume);
    }
}
