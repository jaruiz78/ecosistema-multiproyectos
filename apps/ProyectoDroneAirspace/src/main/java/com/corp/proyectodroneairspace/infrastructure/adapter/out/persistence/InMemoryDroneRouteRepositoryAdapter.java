package com.corp.proyectodroneairspace.infrastructure.adapter.out.persistence;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import com.corp.proyectodroneairspace.domain.port.out.DroneRouteRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDroneRouteRepositoryAdapter implements DroneRouteRepositoryPort {

    private final ConcurrentMap<String, DroneRoute> storage = new ConcurrentHashMap<>();

    @Override
    public DroneRoute save(DroneRoute entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DroneRoute> findById(String id, String tenantId) {
        DroneRoute entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
