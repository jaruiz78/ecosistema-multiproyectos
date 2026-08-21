package com.corp.proyectoquantummaterialsgraphene.application.service;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import com.corp.proyectoquantummaterialsgraphene.domain.port.out.GrapheneRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
