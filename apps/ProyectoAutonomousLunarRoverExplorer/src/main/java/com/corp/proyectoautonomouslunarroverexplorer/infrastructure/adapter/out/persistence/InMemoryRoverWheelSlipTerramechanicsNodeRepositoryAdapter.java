package com.corp.proyectoautonomouslunarroverexplorer.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomouslunarroverexplorer.domain.model.RoverWheelSlipTerramechanicsNode;
import com.corp.proyectoautonomouslunarroverexplorer.domain.port.out.RoverWheelSlipTerramechanicsNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryRoverWheelSlipTerramechanicsNodeRepositoryAdapter implements RoverWheelSlipTerramechanicsNodeRepositoryPort {

    private final ConcurrentMap<String, RoverWheelSlipTerramechanicsNode> storage = new ConcurrentHashMap<>();

    @Override
    public RoverWheelSlipTerramechanicsNode save(RoverWheelSlipTerramechanicsNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RoverWheelSlipTerramechanicsNode> findById(String id, String tenantId) {
        RoverWheelSlipTerramechanicsNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
