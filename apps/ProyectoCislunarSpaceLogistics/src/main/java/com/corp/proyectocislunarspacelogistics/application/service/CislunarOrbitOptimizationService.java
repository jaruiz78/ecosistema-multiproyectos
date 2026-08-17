package com.corp.proyectocislunarspacelogistics.application.service;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryRepositoryPort;

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
