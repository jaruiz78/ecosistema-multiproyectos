package com.corp.proyectocislunarspacelogistics.infrastructure.adapter.out.persistence;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLagrangeTrajectoryRepositoryAdapter implements LagrangeTrajectoryRepositoryPort {

    private final Map<String, LagrangeTransferTrajectory> store = new ConcurrentHashMap<>();

    @Override
    public LagrangeTransferTrajectory save(LagrangeTransferTrajectory trajectory) {
        store.put(trajectory.missionId(), trajectory);
        return trajectory;
    }

    @Override
    public Optional<LagrangeTransferTrajectory> findById(String missionId) {
        return Optional.ofNullable(store.get(missionId));
    }
}
