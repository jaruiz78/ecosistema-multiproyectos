package com.corp.proyectoquantumdotinfraredcamera.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumdotinfraredcamera.domain.model.QdipInfraredPixelMatrixBatch;
import com.corp.proyectoquantumdotinfraredcamera.domain.port.out.QdipInfraredPixelMatrixBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQdipInfraredPixelMatrixBatchRepositoryAdapter implements QdipInfraredPixelMatrixBatchRepositoryPort {

    private final ConcurrentMap<String, QdipInfraredPixelMatrixBatch> storage = new ConcurrentHashMap<>();

    @Override
    public QdipInfraredPixelMatrixBatch save(QdipInfraredPixelMatrixBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QdipInfraredPixelMatrixBatch> findById(String id, String tenantId) {
        QdipInfraredPixelMatrixBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
