package com.corp.proyectoquantummaterialsgraphene.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import com.corp.proyectoquantummaterialsgraphene.domain.port.out.GrapheneRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
