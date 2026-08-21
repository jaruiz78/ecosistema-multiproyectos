package com.corp.proyectocislunarspacelogistics.application.service;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CislunarOrbitOptimizationService {

    private final LagrangeTrajectoryRepositoryPort repositoryPort;

    public CislunarOrbitOptimizationService(LagrangeTrajectoryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public LagrangeTransferTrajectory planCislunarTransfer(String missionId, String lagrangePoint) {
        LagrangeTransferTrajectory trajectory = LagrangeTransferTrajectory.create(missionId, lagrangePoint);
        return repositoryPort.save(trajectory);
    }
}
