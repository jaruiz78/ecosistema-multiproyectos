package com.corp.proyectohydrodynamicdamfloodrouting.domain.port.in;

import com.corp.proyectohydrodynamicdamfloodrouting.domain.model.FloodInundationGridCellNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageFloodInundationGridCellNodeUseCase {
    FloodInundationGridCellNode createFloodInundationGridCellNode(String tenantId, String title, double value);
    Optional<FloodInundationGridCellNode> findFloodInundationGridCellNodeById(String id, String tenantId);
    FloodInundationGridCellNode processOptimization(String id, String tenantId);
}
