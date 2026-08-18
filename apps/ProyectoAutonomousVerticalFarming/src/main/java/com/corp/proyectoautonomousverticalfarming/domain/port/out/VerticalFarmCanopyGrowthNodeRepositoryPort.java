package com.corp.proyectoautonomousverticalfarming.domain.port.out;

import com.corp.proyectoautonomousverticalfarming.domain.model.VerticalFarmCanopyGrowthNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VerticalFarmCanopyGrowthNodeRepositoryPort {
    VerticalFarmCanopyGrowthNode save(VerticalFarmCanopyGrowthNode entity);
    Optional<VerticalFarmCanopyGrowthNode> findById(String id, String tenantId);
}
