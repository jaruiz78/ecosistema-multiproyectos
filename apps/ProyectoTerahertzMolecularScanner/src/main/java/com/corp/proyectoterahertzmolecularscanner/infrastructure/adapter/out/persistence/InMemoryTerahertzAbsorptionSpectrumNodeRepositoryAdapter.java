package com.corp.proyectoterahertzmolecularscanner.infrastructure.adapter.out.persistence;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import com.corp.proyectoterahertzmolecularscanner.domain.port.out.TerahertzAbsorptionSpectrumNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryTerahertzAbsorptionSpectrumNodeRepositoryAdapter implements TerahertzAbsorptionSpectrumNodeRepositoryPort {

    private final ConcurrentMap<String, TerahertzAbsorptionSpectrumNode> storage = new ConcurrentHashMap<>();

    @Override
    public TerahertzAbsorptionSpectrumNode save(TerahertzAbsorptionSpectrumNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TerahertzAbsorptionSpectrumNode> findById(String id, String tenantId) {
        TerahertzAbsorptionSpectrumNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
