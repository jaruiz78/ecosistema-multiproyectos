package com.corp.proyectoelectrodynamictetherdeorbit.domain.port.out;

import com.corp.proyectoelectrodynamictetherdeorbit.domain.model.TetherLorentzDragForceDeorbitNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface TetherLorentzDragForceDeorbitNodeRepositoryPort {
    TetherLorentzDragForceDeorbitNode save(TetherLorentzDragForceDeorbitNode entity);
    Optional<TetherLorentzDragForceDeorbitNode> findById(String id, String tenantId);
}
