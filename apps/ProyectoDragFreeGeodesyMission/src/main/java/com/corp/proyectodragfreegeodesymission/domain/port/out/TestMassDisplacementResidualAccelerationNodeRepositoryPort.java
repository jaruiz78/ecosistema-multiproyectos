package com.corp.proyectodragfreegeodesymission.domain.port.out;

import com.corp.proyectodragfreegeodesymission.domain.model.TestMassDisplacementResidualAccelerationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface TestMassDisplacementResidualAccelerationNodeRepositoryPort {
    TestMassDisplacementResidualAccelerationNode save(TestMassDisplacementResidualAccelerationNode entity);
    Optional<TestMassDisplacementResidualAccelerationNode> findById(String id, String tenantId);
}
