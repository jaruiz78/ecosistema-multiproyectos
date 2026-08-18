package com.corp.proyectoagrobiorobotics.infrastructure.adapter.out.persistence;

import com.corp.proyectoagrobiorobotics.domain.model.AgriRobotSwarmAgent;
import com.corp.proyectoagrobiorobotics.domain.port.out.AgriRobotSwarmAgentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAgriRobotSwarmAgentRepositoryAdapter implements AgriRobotSwarmAgentRepositoryPort {

    private final ConcurrentMap<String, AgriRobotSwarmAgent> storage = new ConcurrentHashMap<>();

    @Override
    public AgriRobotSwarmAgent save(AgriRobotSwarmAgent entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AgriRobotSwarmAgent> findById(String id, String tenantId) {
        AgriRobotSwarmAgent entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
