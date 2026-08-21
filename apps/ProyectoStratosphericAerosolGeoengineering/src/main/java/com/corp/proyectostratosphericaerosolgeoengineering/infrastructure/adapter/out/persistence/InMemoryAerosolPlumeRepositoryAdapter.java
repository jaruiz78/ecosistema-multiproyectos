package com.corp.proyectostratosphericaerosolgeoengineering.infrastructure.adapter.out.persistence;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out.AerosolPlumeRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
