package com.corp.proyectohydrodynamicdamfloodrouting.domain.port.out;

import com.corp.proyectohydrodynamicdamfloodrouting.domain.model.FloodInundationGridCellNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface FloodInundationGridCellNodeRepositoryPort {
    FloodInundationGridCellNode save(FloodInundationGridCellNode entity);
    Optional<FloodInundationGridCellNode> findById(String id, String tenantId);
}
