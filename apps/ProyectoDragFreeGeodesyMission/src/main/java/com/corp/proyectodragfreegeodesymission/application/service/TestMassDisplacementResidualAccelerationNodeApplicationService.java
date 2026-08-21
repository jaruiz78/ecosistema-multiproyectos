package com.corp.proyectodragfreegeodesymission.application.service;

import com.corp.proyectodragfreegeodesymission.domain.model.TestMassDisplacementResidualAccelerationNode;
import com.corp.proyectodragfreegeodesymission.domain.port.in.ManageTestMassDisplacementResidualAccelerationNodeUseCase;
import com.corp.proyectodragfreegeodesymission.domain.port.out.TestMassDisplacementResidualAccelerationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TestMassDisplacementResidualAccelerationNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class TestMassDisplacementResidualAccelerationNodeApplicationService implements ManageTestMassDisplacementResidualAccelerationNodeUseCase {

    private final TestMassDisplacementResidualAccelerationNodeRepositoryPort repositoryPort;

    public TestMassDisplacementResidualAccelerationNodeApplicationService(TestMassDisplacementResidualAccelerationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TestMassDisplacementResidualAccelerationNode createTestMassDisplacementResidualAccelerationNode(String tenantId, String title, double value) {
        TestMassDisplacementResidualAccelerationNode entity = new TestMassDisplacementResidualAccelerationNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<TestMassDisplacementResidualAccelerationNode> findTestMassDisplacementResidualAccelerationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TestMassDisplacementResidualAccelerationNode processOptimization(String id, String tenantId) {
        TestMassDisplacementResidualAccelerationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TestMassDisplacementResidualAccelerationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
