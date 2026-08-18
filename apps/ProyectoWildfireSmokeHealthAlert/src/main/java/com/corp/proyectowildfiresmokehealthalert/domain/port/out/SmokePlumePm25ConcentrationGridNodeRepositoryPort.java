package com.corp.proyectowildfiresmokehealthalert.domain.port.out;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SmokePlumePm25ConcentrationGridNodeRepositoryPort {
    SmokePlumePm25ConcentrationGridNode save(SmokePlumePm25ConcentrationGridNode entity);
    Optional<SmokePlumePm25ConcentrationGridNode> findById(String id, String tenantId);
}
