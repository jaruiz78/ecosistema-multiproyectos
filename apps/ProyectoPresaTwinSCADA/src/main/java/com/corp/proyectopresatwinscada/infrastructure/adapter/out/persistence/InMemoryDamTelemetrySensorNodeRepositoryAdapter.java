package com.corp.proyectopresatwinscada.infrastructure.adapter.out.persistence;

import com.corp.proyectopresatwinscada.domain.model.DamTelemetrySensorNode;
import com.corp.proyectopresatwinscada.domain.port.out.DamTelemetrySensorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDamTelemetrySensorNodeRepositoryAdapter implements DamTelemetrySensorNodeRepositoryPort {

    private final ConcurrentMap<String, DamTelemetrySensorNode> storage = new ConcurrentHashMap<>();

    @Override
    public DamTelemetrySensorNode save(DamTelemetrySensorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DamTelemetrySensorNode> findById(String id, String tenantId) {
        DamTelemetrySensorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
