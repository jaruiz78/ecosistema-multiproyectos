package com.corp.proyectoemergencygeogrid.infrastructure.adapter.out.persistence;

import com.corp.proyectoemergencygeogrid.domain.model.EmergencyGeoGrid;
import com.corp.proyectoemergencygeogrid.domain.port.out.EmergencyGeoGridRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryEmergencyGeoGridRepositoryAdapter implements EmergencyGeoGridRepositoryPort {

    private final ConcurrentMap<String, EmergencyGeoGrid> storage = new ConcurrentHashMap<>();

    @Override
    public EmergencyGeoGrid save(EmergencyGeoGrid entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EmergencyGeoGrid> findById(String id, String tenantId) {
        EmergencyGeoGrid entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
