package com.corp.proyectohypersonicintercontinentalfreight.infrastructure.adapter.out.persistence;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import com.corp.proyectohypersonicintercontinentalfreight.domain.port.out.ScramjetCombustionPressureRatioNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryScramjetCombustionPressureRatioNodeRepositoryAdapter implements ScramjetCombustionPressureRatioNodeRepositoryPort {

    private final ConcurrentMap<String, ScramjetCombustionPressureRatioNode> storage = new ConcurrentHashMap<>();

    @Override
    public ScramjetCombustionPressureRatioNode save(ScramjetCombustionPressureRatioNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ScramjetCombustionPressureRatioNode> findById(String id, String tenantId) {
        ScramjetCombustionPressureRatioNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
