package com.corp.proyectospaceweathergriddefense.domain.port.in;

import com.corp.proyectospaceweathergriddefense.domain.model.GicTransformerNeutralCurrentAlertNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGicTransformerNeutralCurrentAlertNodeUseCase {
    GicTransformerNeutralCurrentAlertNode createGicTransformerNeutralCurrentAlertNode(String tenantId, String title, double value);
    Optional<GicTransformerNeutralCurrentAlertNode> findGicTransformerNeutralCurrentAlertNodeById(String id, String tenantId);
    GicTransformerNeutralCurrentAlertNode processOptimization(String id, String tenantId);
}
