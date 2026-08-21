package com.corp.proyectomaritimeautonomousfleet.infrastructure.adapter.out.persistence;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import com.corp.proyectomaritimeautonomousfleet.domain.port.out.AutonomousVesselVoyageRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAutonomousVesselVoyageRepositoryAdapter implements AutonomousVesselVoyageRepositoryPort {

    private final ConcurrentMap<String, AutonomousVesselVoyage> storage = new ConcurrentHashMap<>();

    @Override
    public AutonomousVesselVoyage save(AutonomousVesselVoyage entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AutonomousVesselVoyage> findById(String id, String tenantId) {
        AutonomousVesselVoyage entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
