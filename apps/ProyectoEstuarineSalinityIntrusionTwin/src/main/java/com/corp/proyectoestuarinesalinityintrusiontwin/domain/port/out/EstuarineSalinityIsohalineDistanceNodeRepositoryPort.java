package com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.out;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EstuarineSalinityIsohalineDistanceNodeRepositoryPort {
    EstuarineSalinityIsohalineDistanceNode save(EstuarineSalinityIsohalineDistanceNode entity);
    Optional<EstuarineSalinityIsohalineDistanceNode> findById(String id, String tenantId);
}
