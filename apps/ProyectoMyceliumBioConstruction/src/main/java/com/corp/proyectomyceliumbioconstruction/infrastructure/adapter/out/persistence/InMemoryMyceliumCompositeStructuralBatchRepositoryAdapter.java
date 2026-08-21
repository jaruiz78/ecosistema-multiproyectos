package com.corp.proyectomyceliumbioconstruction.infrastructure.adapter.out.persistence;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import com.corp.proyectomyceliumbioconstruction.domain.port.out.MyceliumCompositeStructuralBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMyceliumCompositeStructuralBatchRepositoryAdapter implements MyceliumCompositeStructuralBatchRepositoryPort {

    private final ConcurrentMap<String, MyceliumCompositeStructuralBatch> storage = new ConcurrentHashMap<>();

    @Override
    public MyceliumCompositeStructuralBatch save(MyceliumCompositeStructuralBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MyceliumCompositeStructuralBatch> findById(String id, String tenantId) {
        MyceliumCompositeStructuralBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
