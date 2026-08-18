package com.corp.proyectohighpowersolarelectrictug.domain.port.out;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SepXenonMassFlowThrustNodeRepositoryPort {
    SepXenonMassFlowThrustNode save(SepXenonMassFlowThrustNode entity);
    Optional<SepXenonMassFlowThrustNode> findById(String id, String tenantId);
}
