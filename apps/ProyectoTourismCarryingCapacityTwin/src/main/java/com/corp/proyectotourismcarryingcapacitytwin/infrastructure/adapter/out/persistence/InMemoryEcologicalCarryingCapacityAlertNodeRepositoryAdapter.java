package com.corp.proyectotourismcarryingcapacitytwin.infrastructure.adapter.out.persistence;

import com.corp.proyectotourismcarryingcapacitytwin.domain.model.EcologicalCarryingCapacityAlertNode;
import com.corp.proyectotourismcarryingcapacitytwin.domain.port.out.EcologicalCarryingCapacityAlertNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEcologicalCarryingCapacityAlertNodeRepositoryAdapter implements EcologicalCarryingCapacityAlertNodeRepositoryPort {

    private final ConcurrentMap<String, EcologicalCarryingCapacityAlertNode> storage = new ConcurrentHashMap<>();

    @Override
    public EcologicalCarryingCapacityAlertNode save(EcologicalCarryingCapacityAlertNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EcologicalCarryingCapacityAlertNode> findById(String id, String tenantId) {
        EcologicalCarryingCapacityAlertNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
