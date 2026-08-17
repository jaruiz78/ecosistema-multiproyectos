package com.corp.proyectoquantummaterialsgraphene.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import com.corp.proyectoquantummaterialsgraphene.domain.port.out.GrapheneRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGrapheneRepositoryAdapter implements GrapheneRepositoryPort {

    private final Map<String, GrapheneHeterostructure> database = new ConcurrentHashMap<>();

    @Override
    public GrapheneHeterostructure save(GrapheneHeterostructure structure) {
        database.put(structure.sampleId(), structure);
        return structure;
    }

    @Override
    public Optional<GrapheneHeterostructure> findById(String sampleId) {
        return Optional.ofNullable(database.get(sampleId));
    }
}
