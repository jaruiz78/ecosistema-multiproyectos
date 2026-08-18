package com.corp.proyectoneuralbciaccessibilitycontrol.infrastructure.adapter.out.persistence;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.out.BciNeuralMotorIntentEventNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBciNeuralMotorIntentEventNodeRepositoryAdapter implements BciNeuralMotorIntentEventNodeRepositoryPort {

    private final ConcurrentMap<String, BciNeuralMotorIntentEventNode> storage = new ConcurrentHashMap<>();

    @Override
    public BciNeuralMotorIntentEventNode save(BciNeuralMotorIntentEventNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BciNeuralMotorIntentEventNode> findById(String id, String tenantId) {
        BciNeuralMotorIntentEventNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
