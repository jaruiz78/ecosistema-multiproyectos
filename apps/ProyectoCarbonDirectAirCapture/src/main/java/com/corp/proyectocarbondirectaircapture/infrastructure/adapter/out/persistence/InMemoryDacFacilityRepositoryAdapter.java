package com.corp.proyectocarbondirectaircapture.infrastructure.adapter.out.persistence;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import com.corp.proyectocarbondirectaircapture.domain.port.out.DacFacilityRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDacFacilityRepositoryAdapter implements DacFacilityRepositoryPort {

    private final Map<String, DirectAirCaptureFacility> storage = new ConcurrentHashMap<>();

    @Override
    public DirectAirCaptureFacility save(DirectAirCaptureFacility facility) {
        storage.put(facility.facilityId(), facility);
        return facility;
    }

    @Override
    public Optional<DirectAirCaptureFacility> findById(String facilityId) {
        return Optional.ofNullable(storage.get(facilityId));
    }
}
