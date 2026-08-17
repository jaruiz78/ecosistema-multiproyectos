package com.corp.proyectosmartgridstoragevpp.infrastructure.adapter.out.persistence;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import com.corp.proyectosmartgridstoragevpp.domain.port.out.BatteryStorageRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBatteryStorageRepositoryAdapter implements BatteryStorageRepositoryPort {

    private final Map<String, BatteryStorageUnit> storage = new ConcurrentHashMap<>();

    @Override
    public BatteryStorageUnit save(BatteryStorageUnit unit) {
        storage.put(unit.batteryId(), unit);
        return unit;
    }

    @Override
    public Optional<BatteryStorageUnit> findById(String batteryId) {
        return Optional.ofNullable(storage.get(batteryId));
    }
}
