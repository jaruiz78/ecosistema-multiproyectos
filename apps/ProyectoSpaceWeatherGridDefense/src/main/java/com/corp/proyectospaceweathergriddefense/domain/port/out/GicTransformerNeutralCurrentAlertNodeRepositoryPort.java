package com.corp.proyectospaceweathergriddefense.domain.port.out;

import com.corp.proyectospaceweathergriddefense.domain.model.GicTransformerNeutralCurrentAlertNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GicTransformerNeutralCurrentAlertNodeRepositoryPort {
    GicTransformerNeutralCurrentAlertNode save(GicTransformerNeutralCurrentAlertNode entity);
    Optional<GicTransformerNeutralCurrentAlertNode> findById(String id, String tenantId);
}
