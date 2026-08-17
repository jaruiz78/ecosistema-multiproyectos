package com.corp.proyectoquantummaterialsgraphene.domain.port.out;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import java.util.Optional;

public interface GrapheneRepositoryPort {
    GrapheneHeterostructure save(GrapheneHeterostructure structure);
    Optional<GrapheneHeterostructure> findById(String sampleId);
}
