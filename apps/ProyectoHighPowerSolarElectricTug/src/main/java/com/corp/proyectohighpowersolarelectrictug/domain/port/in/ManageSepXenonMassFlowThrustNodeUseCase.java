package com.corp.proyectohighpowersolarelectrictug.domain.port.in;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSepXenonMassFlowThrustNodeUseCase {
    SepXenonMassFlowThrustNode createSepXenonMassFlowThrustNode(String tenantId, String title, double value);
    Optional<SepXenonMassFlowThrustNode> findSepXenonMassFlowThrustNodeById(String id, String tenantId);
    SepXenonMassFlowThrustNode processOptimization(String id, String tenantId);
}
