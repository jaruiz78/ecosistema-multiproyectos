package com.corp.proyectocoastalsurgeflooddefense.infrastructure.adapter.out.persistence;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import com.corp.proyectocoastalsurgeflooddefense.domain.port.out.StormSurgeElevationForecastNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryStormSurgeElevationForecastNodeRepositoryAdapter implements StormSurgeElevationForecastNodeRepositoryPort {

    private final ConcurrentMap<String, StormSurgeElevationForecastNode> storage = new ConcurrentHashMap<>();

    @Override
    public StormSurgeElevationForecastNode save(StormSurgeElevationForecastNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<StormSurgeElevationForecastNode> findById(String id, String tenantId) {
        StormSurgeElevationForecastNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
