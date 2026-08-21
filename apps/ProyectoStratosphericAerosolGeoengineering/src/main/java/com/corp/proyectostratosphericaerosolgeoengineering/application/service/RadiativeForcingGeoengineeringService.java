package com.corp.proyectostratosphericaerosolgeoengineering.application.service;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out.AerosolPlumeRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
