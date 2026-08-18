package com.corp.proyectomyceliumbioconstruction.infrastructure.adapter.out.persistence;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import com.corp.proyectomyceliumbioconstruction.domain.port.out.MyceliumCompositeStructuralBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
