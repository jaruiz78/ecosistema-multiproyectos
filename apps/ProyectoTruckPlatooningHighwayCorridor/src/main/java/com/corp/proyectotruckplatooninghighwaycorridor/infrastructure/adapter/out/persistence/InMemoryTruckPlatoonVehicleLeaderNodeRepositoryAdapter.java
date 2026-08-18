package com.corp.proyectotruckplatooninghighwaycorridor.infrastructure.adapter.out.persistence;

import com.corp.proyectotruckplatooninghighwaycorridor.domain.model.TruckPlatoonVehicleLeaderNode;
import com.corp.proyectotruckplatooninghighwaycorridor.domain.port.out.TruckPlatoonVehicleLeaderNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryTruckPlatoonVehicleLeaderNodeRepositoryAdapter implements TruckPlatoonVehicleLeaderNodeRepositoryPort {

    private final ConcurrentMap<String, TruckPlatoonVehicleLeaderNode> storage = new ConcurrentHashMap<>();

    @Override
    public TruckPlatoonVehicleLeaderNode save(TruckPlatoonVehicleLeaderNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TruckPlatoonVehicleLeaderNode> findById(String id, String tenantId) {
        TruckPlatoonVehicleLeaderNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
