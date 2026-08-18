package com.corp.proyectootecmarinecleanenergy.infrastructure.adapter.out.persistence;

import com.corp.proyectootecmarinecleanenergy.domain.model.OtecThermalGradientTurbineNode;
import com.corp.proyectootecmarinecleanenergy.domain.port.out.OtecThermalGradientTurbineNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryOtecThermalGradientTurbineNodeRepositoryAdapter implements OtecThermalGradientTurbineNodeRepositoryPort {

    private final ConcurrentMap<String, OtecThermalGradientTurbineNode> storage = new ConcurrentHashMap<>();

    @Override
    public OtecThermalGradientTurbineNode save(OtecThermalGradientTurbineNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<OtecThermalGradientTurbineNode> findById(String id, String tenantId) {
        OtecThermalGradientTurbineNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
