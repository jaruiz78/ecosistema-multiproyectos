package com.corp.proyectoatmosphericwaterharvesting.infrastructure.adapter.out.persistence;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import com.corp.proyectoatmosphericwaterharvesting.domain.port.out.MofWaterAdsorptionChamberNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMofWaterAdsorptionChamberNodeRepositoryAdapter implements MofWaterAdsorptionChamberNodeRepositoryPort {

    private final ConcurrentMap<String, MofWaterAdsorptionChamberNode> storage = new ConcurrentHashMap<>();

    @Override
    public MofWaterAdsorptionChamberNode save(MofWaterAdsorptionChamberNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MofWaterAdsorptionChamberNode> findById(String id, String tenantId) {
        MofWaterAdsorptionChamberNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
