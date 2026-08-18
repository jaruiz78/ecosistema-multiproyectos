package com.corp.proyectoatomicforcenanotopography.infrastructure.adapter.out.persistence;

import com.corp.proyectoatomicforcenanotopography.domain.model.AfmCantileverDeflectionScanNode;
import com.corp.proyectoatomicforcenanotopography.domain.port.out.AfmCantileverDeflectionScanNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAfmCantileverDeflectionScanNodeRepositoryAdapter implements AfmCantileverDeflectionScanNodeRepositoryPort {

    private final ConcurrentMap<String, AfmCantileverDeflectionScanNode> storage = new ConcurrentHashMap<>();

    @Override
    public AfmCantileverDeflectionScanNode save(AfmCantileverDeflectionScanNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AfmCantileverDeflectionScanNode> findById(String id, String tenantId) {
        AfmCantileverDeflectionScanNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
