package com.corp.proyectoelectrodynamictetherdeorbit.domain.port.in;

import com.corp.proyectoelectrodynamictetherdeorbit.domain.model.TetherLorentzDragForceDeorbitNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageTetherLorentzDragForceDeorbitNodeUseCase {
    TetherLorentzDragForceDeorbitNode createTetherLorentzDragForceDeorbitNode(String tenantId, String title, double value);
    Optional<TetherLorentzDragForceDeorbitNode> findTetherLorentzDragForceDeorbitNodeById(String id, String tenantId);
    TetherLorentzDragForceDeorbitNode processOptimization(String id, String tenantId);
}
