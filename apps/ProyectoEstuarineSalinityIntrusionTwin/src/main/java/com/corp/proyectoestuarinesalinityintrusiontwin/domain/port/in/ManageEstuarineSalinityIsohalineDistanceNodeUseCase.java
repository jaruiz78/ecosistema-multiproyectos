package com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.in;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEstuarineSalinityIsohalineDistanceNodeUseCase {
    EstuarineSalinityIsohalineDistanceNode createEstuarineSalinityIsohalineDistanceNode(String tenantId, String title, double value);
    Optional<EstuarineSalinityIsohalineDistanceNode> findEstuarineSalinityIsohalineDistanceNodeById(String id, String tenantId);
    EstuarineSalinityIsohalineDistanceNode processOptimization(String id, String tenantId);
}
