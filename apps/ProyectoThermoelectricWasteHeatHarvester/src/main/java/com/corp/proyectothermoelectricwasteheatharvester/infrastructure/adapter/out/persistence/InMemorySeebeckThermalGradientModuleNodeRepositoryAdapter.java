package com.corp.proyectothermoelectricwasteheatharvester.infrastructure.adapter.out.persistence;

import com.corp.proyectothermoelectricwasteheatharvester.domain.model.SeebeckThermalGradientModuleNode;
import com.corp.proyectothermoelectricwasteheatharvester.domain.port.out.SeebeckThermalGradientModuleNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySeebeckThermalGradientModuleNodeRepositoryAdapter implements SeebeckThermalGradientModuleNodeRepositoryPort {

    private final ConcurrentMap<String, SeebeckThermalGradientModuleNode> storage = new ConcurrentHashMap<>();

    @Override
    public SeebeckThermalGradientModuleNode save(SeebeckThermalGradientModuleNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SeebeckThermalGradientModuleNode> findById(String id, String tenantId) {
        SeebeckThermalGradientModuleNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
