package com.corp.proyectocubesatconstellationadcs.domain.port.in;

import com.corp.proyectocubesatconstellationadcs.domain.model.CubesatAttitudeQuaternionPointingNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCubesatAttitudeQuaternionPointingNodeUseCase {
    CubesatAttitudeQuaternionPointingNode createCubesatAttitudeQuaternionPointingNode(String tenantId, String title, double value);
    Optional<CubesatAttitudeQuaternionPointingNode> findCubesatAttitudeQuaternionPointingNodeById(String id, String tenantId);
    CubesatAttitudeQuaternionPointingNode processOptimization(String id, String tenantId);
}
