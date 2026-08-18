package com.corp.proyectotsunamiearlywarningsystem.infrastructure.adapter.out.persistence;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.out.TsunamiWaveformPressureSensorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryTsunamiWaveformPressureSensorNodeRepositoryAdapter implements TsunamiWaveformPressureSensorNodeRepositoryPort {

    private final ConcurrentMap<String, TsunamiWaveformPressureSensorNode> storage = new ConcurrentHashMap<>();

    @Override
    public TsunamiWaveformPressureSensorNode save(TsunamiWaveformPressureSensorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TsunamiWaveformPressureSensorNode> findById(String id, String tenantId) {
        TsunamiWaveformPressureSensorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
