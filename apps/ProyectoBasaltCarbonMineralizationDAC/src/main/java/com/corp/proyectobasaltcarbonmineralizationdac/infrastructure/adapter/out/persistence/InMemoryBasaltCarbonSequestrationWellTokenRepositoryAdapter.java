package com.corp.proyectobasaltcarbonmineralizationdac.infrastructure.adapter.out.persistence;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.out.BasaltCarbonSequestrationWellTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBasaltCarbonSequestrationWellTokenRepositoryAdapter implements BasaltCarbonSequestrationWellTokenRepositoryPort {

    private final ConcurrentMap<String, BasaltCarbonSequestrationWellToken> storage = new ConcurrentHashMap<>();

    @Override
    public BasaltCarbonSequestrationWellToken save(BasaltCarbonSequestrationWellToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BasaltCarbonSequestrationWellToken> findById(String id, String tenantId) {
        BasaltCarbonSequestrationWellToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
