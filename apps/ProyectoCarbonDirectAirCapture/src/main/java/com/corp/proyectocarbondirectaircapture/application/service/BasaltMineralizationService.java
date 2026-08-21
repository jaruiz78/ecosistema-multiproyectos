package com.corp.proyectocarbondirectaircapture.application.service;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import com.corp.proyectocarbondirectaircapture.domain.port.out.DacFacilityRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BasaltMineralizationService {

    private final DacFacilityRepositoryPort repositoryPort;

    public BasaltMineralizationService(DacFacilityRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public DirectAirCaptureFacility injectCo2ToBasalt(String facilityId, double batchTonnes) {
        DirectAirCaptureFacility facility = repositoryPort.findById(facilityId)
                .orElseGet(() -> DirectAirCaptureFacility.create(facilityId, 100.0));

        DirectAirCaptureFacility updated = facility.recordMineralizationBatch(batchTonnes);
        return repositoryPort.save(updated);
    }
}
