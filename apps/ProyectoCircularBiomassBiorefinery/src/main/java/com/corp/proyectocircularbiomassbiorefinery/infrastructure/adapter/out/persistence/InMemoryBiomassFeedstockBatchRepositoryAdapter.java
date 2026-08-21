package com.corp.proyectocircularbiomassbiorefinery.infrastructure.adapter.out.persistence;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import com.corp.proyectocircularbiomassbiorefinery.domain.port.out.BiomassFeedstockBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryBiomassFeedstockBatchRepositoryAdapter implements BiomassFeedstockBatchRepositoryPort {

    private final ConcurrentMap<String, BiomassFeedstockBatch> storage = new ConcurrentHashMap<>();

    @Override
    public BiomassFeedstockBatch save(BiomassFeedstockBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BiomassFeedstockBatch> findById(String id, String tenantId) {
        BiomassFeedstockBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
