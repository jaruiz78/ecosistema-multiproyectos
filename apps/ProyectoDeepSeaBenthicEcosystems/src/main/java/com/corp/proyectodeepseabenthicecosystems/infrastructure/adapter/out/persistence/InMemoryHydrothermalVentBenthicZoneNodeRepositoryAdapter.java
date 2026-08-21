package com.corp.proyectodeepseabenthicecosystems.infrastructure.adapter.out.persistence;

import com.corp.proyectodeepseabenthicecosystems.domain.model.HydrothermalVentBenthicZoneNode;
import com.corp.proyectodeepseabenthicecosystems.domain.port.out.HydrothermalVentBenthicZoneNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHydrothermalVentBenthicZoneNodeRepositoryAdapter implements HydrothermalVentBenthicZoneNodeRepositoryPort {

    private final ConcurrentMap<String, HydrothermalVentBenthicZoneNode> storage = new ConcurrentHashMap<>();

    @Override
    public HydrothermalVentBenthicZoneNode save(HydrothermalVentBenthicZoneNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HydrothermalVentBenthicZoneNode> findById(String id, String tenantId) {
        HydrothermalVentBenthicZoneNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
