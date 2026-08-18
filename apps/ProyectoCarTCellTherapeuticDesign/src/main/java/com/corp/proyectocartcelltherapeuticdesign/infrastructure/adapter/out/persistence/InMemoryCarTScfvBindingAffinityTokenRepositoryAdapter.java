package com.corp.proyectocartcelltherapeuticdesign.infrastructure.adapter.out.persistence;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import com.corp.proyectocartcelltherapeuticdesign.domain.port.out.CarTScfvBindingAffinityTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCarTScfvBindingAffinityTokenRepositoryAdapter implements CarTScfvBindingAffinityTokenRepositoryPort {

    private final ConcurrentMap<String, CarTScfvBindingAffinityToken> storage = new ConcurrentHashMap<>();

    @Override
    public CarTScfvBindingAffinityToken save(CarTScfvBindingAffinityToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CarTScfvBindingAffinityToken> findById(String id, String tenantId) {
        CarTScfvBindingAffinityToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
