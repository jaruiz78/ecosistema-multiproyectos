package com.corp.proyectodragfreegeodesymission.infrastructure.adapter.out.persistence;

import com.corp.proyectodragfreegeodesymission.domain.model.TestMassDisplacementResidualAccelerationNode;
import com.corp.proyectodragfreegeodesymission.domain.port.out.TestMassDisplacementResidualAccelerationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryTestMassDisplacementResidualAccelerationNodeRepositoryAdapter implements TestMassDisplacementResidualAccelerationNodeRepositoryPort {

    private final ConcurrentMap<String, TestMassDisplacementResidualAccelerationNode> storage = new ConcurrentHashMap<>();

    @Override
    public TestMassDisplacementResidualAccelerationNode save(TestMassDisplacementResidualAccelerationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TestMassDisplacementResidualAccelerationNode> findById(String id, String tenantId) {
        TestMassDisplacementResidualAccelerationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
