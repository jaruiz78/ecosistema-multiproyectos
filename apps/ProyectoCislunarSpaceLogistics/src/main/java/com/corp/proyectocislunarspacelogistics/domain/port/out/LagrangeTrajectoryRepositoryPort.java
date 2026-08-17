package com.corp.proyectocislunarspacelogistics.domain.port.out;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import java.util.Optional;

public interface LagrangeTrajectoryRepositoryPort {
    LagrangeTransferTrajectory save(LagrangeTransferTrajectory trajectory);
    Optional<LagrangeTransferTrajectory> findById(String missionId);
}
