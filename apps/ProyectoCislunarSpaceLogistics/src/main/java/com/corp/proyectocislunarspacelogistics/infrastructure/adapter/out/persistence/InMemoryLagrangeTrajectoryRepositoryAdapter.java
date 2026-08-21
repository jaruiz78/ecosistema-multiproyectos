package com.corp.proyectocislunarspacelogistics.infrastructure.adapter.out.persistence;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
