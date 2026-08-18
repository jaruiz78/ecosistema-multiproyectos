package com.corp.proyectomicrogravitybiotechlaboratory.domain.port.in;

import com.corp.proyectomicrogravitybiotechlaboratory.domain.model.MicrogravityGProfileAccelerationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMicrogravityGProfileAccelerationNodeUseCase {
    MicrogravityGProfileAccelerationNode createMicrogravityGProfileAccelerationNode(String tenantId, String title, double value);
    Optional<MicrogravityGProfileAccelerationNode> findMicrogravityGProfileAccelerationNodeById(String id, String tenantId);
    MicrogravityGProfileAccelerationNode processOptimization(String id, String tenantId);
}
