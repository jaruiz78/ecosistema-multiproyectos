package com.corp.proyectostratosphericaerosolgeoengineering.infrastructure.adapter.out.persistence;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out.AerosolPlumeRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAerosolPlumeRepositoryAdapter implements AerosolPlumeRepositoryPort {

    private final Map<String, StratosphericAerosolPlume> plumeStore = new ConcurrentHashMap<>();

    @Override
    public StratosphericAerosolPlume save(StratosphericAerosolPlume plume) {
        plumeStore.put(plume.injectionId(), plume);
        return plume;
    }

    @Override
    public Optional<StratosphericAerosolPlume> findById(String injectionId) {
        return Optional.ofNullable(plumeStore.get(injectionId));
    }
}
