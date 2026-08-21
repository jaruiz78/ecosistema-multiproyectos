package com.corp.proyectosmartgridstoragevpp.infrastructure.adapter.out.persistence;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import com.corp.proyectosmartgridstoragevpp.domain.port.out.BatteryStorageRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
