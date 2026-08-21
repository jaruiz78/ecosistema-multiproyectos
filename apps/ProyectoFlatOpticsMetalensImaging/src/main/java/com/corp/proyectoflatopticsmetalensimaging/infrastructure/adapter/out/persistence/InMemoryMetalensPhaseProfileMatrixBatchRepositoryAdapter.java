package com.corp.proyectoflatopticsmetalensimaging.infrastructure.adapter.out.persistence;

import com.corp.proyectoflatopticsmetalensimaging.domain.model.MetalensPhaseProfileMatrixBatch;
import com.corp.proyectoflatopticsmetalensimaging.domain.port.out.MetalensPhaseProfileMatrixBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMetalensPhaseProfileMatrixBatchRepositoryAdapter implements MetalensPhaseProfileMatrixBatchRepositoryPort {

    private final ConcurrentMap<String, MetalensPhaseProfileMatrixBatch> storage = new ConcurrentHashMap<>();

    @Override
    public MetalensPhaseProfileMatrixBatch save(MetalensPhaseProfileMatrixBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MetalensPhaseProfileMatrixBatch> findById(String id, String tenantId) {
        MetalensPhaseProfileMatrixBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
