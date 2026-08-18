package com.corp.proyectostratosphericaerosollidarnetwork.domain.port.in;

import com.corp.proyectostratosphericaerosollidarnetwork.domain.model.AerosolBackscatterExtinctionProfileNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAerosolBackscatterExtinctionProfileNodeUseCase {
    AerosolBackscatterExtinctionProfileNode createAerosolBackscatterExtinctionProfileNode(String tenantId, String title, double value);
    Optional<AerosolBackscatterExtinctionProfileNode> findAerosolBackscatterExtinctionProfileNodeById(String id, String tenantId);
    AerosolBackscatterExtinctionProfileNode processOptimization(String id, String tenantId);
}
