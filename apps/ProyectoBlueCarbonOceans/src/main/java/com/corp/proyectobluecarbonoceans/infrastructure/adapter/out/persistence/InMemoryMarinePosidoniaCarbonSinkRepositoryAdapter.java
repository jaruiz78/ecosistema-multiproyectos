package com.corp.proyectobluecarbonoceans.infrastructure.adapter.out.persistence;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import com.corp.proyectobluecarbonoceans.domain.port.out.MarinePosidoniaCarbonSinkRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMarinePosidoniaCarbonSinkRepositoryAdapter implements MarinePosidoniaCarbonSinkRepositoryPort {

    private final ConcurrentMap<String, MarinePosidoniaCarbonSink> storage = new ConcurrentHashMap<>();

    @Override
    public MarinePosidoniaCarbonSink save(MarinePosidoniaCarbonSink entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MarinePosidoniaCarbonSink> findById(String id, String tenantId) {
        MarinePosidoniaCarbonSink entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
