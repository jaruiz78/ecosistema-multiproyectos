package com.corp.proyectocarbondirectaircapture.application.service;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import com.corp.proyectocarbondirectaircapture.domain.port.out.DacFacilityRepositoryPort;

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
