package com.corp.proyectodragfreegeodesymission.domain.port.in;

import com.corp.proyectodragfreegeodesymission.domain.model.TestMassDisplacementResidualAccelerationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageTestMassDisplacementResidualAccelerationNodeUseCase {
    TestMassDisplacementResidualAccelerationNode createTestMassDisplacementResidualAccelerationNode(String tenantId, String title, double value);
    Optional<TestMassDisplacementResidualAccelerationNode> findTestMassDisplacementResidualAccelerationNodeById(String id, String tenantId);
    TestMassDisplacementResidualAccelerationNode processOptimization(String id, String tenantId);
}
