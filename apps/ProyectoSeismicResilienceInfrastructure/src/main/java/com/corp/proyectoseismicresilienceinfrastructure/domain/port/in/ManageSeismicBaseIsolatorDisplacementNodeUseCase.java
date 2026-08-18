package com.corp.proyectoseismicresilienceinfrastructure.domain.port.in;

import com.corp.proyectoseismicresilienceinfrastructure.domain.model.SeismicBaseIsolatorDisplacementNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSeismicBaseIsolatorDisplacementNodeUseCase {
    SeismicBaseIsolatorDisplacementNode createSeismicBaseIsolatorDisplacementNode(String tenantId, String title, double value);
    Optional<SeismicBaseIsolatorDisplacementNode> findSeismicBaseIsolatorDisplacementNodeById(String id, String tenantId);
    SeismicBaseIsolatorDisplacementNode processOptimization(String id, String tenantId);
}
