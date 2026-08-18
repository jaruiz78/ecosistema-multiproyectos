package com.corp.proyectourbanenergymobilitynexus.infrastructure.adapter.out.persistence;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import com.corp.proyectourbanenergymobilitynexus.domain.port.out.V2GChargingDispatchNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryV2GChargingDispatchNodeRepositoryAdapter implements V2GChargingDispatchNodeRepositoryPort {

    private final ConcurrentMap<String, V2GChargingDispatchNode> storage = new ConcurrentHashMap<>();

    @Override
    public V2GChargingDispatchNode save(V2GChargingDispatchNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<V2GChargingDispatchNode> findById(String id, String tenantId) {
        V2GChargingDispatchNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
