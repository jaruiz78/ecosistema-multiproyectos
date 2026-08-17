package com.corp.proyectoquantummaterialsgraphene.application.service;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import com.corp.proyectoquantummaterialsgraphene.domain.port.out.GrapheneRepositoryPort;

public class GrapheneSuperconductivityService {

    private final GrapheneRepositoryPort repositoryPort;

    public GrapheneSuperconductivityService(GrapheneRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public GrapheneHeterostructure analyzeTwistAngleSample(String sampleId, double angleDeg) {
        GrapheneHeterostructure structure = GrapheneHeterostructure.create(sampleId, angleDeg);
        return repositoryPort.save(structure);
    }
}
