package com.corp.proyectowildfiresmokehealthalert.domain.port.in;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSmokePlumePm25ConcentrationGridNodeUseCase {
    SmokePlumePm25ConcentrationGridNode createSmokePlumePm25ConcentrationGridNode(String tenantId, String title, double value);
    Optional<SmokePlumePm25ConcentrationGridNode> findSmokePlumePm25ConcentrationGridNodeById(String id, String tenantId);
    SmokePlumePm25ConcentrationGridNode processOptimization(String id, String tenantId);
}
