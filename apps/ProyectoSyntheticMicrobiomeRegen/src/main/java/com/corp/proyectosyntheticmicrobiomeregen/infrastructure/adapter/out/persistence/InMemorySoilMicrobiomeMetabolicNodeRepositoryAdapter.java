package com.corp.proyectosyntheticmicrobiomeregen.infrastructure.adapter.out.persistence;

import com.corp.proyectosyntheticmicrobiomeregen.domain.model.SoilMicrobiomeMetabolicNode;
import com.corp.proyectosyntheticmicrobiomeregen.domain.port.out.SoilMicrobiomeMetabolicNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySoilMicrobiomeMetabolicNodeRepositoryAdapter implements SoilMicrobiomeMetabolicNodeRepositoryPort {

    private final ConcurrentMap<String, SoilMicrobiomeMetabolicNode> storage = new ConcurrentHashMap<>();

    @Override
    public SoilMicrobiomeMetabolicNode save(SoilMicrobiomeMetabolicNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SoilMicrobiomeMetabolicNode> findById(String id, String tenantId) {
        SoilMicrobiomeMetabolicNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
