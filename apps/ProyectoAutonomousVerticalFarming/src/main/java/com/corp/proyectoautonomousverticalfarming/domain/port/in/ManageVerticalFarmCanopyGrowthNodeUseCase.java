package com.corp.proyectoautonomousverticalfarming.domain.port.in;

import com.corp.proyectoautonomousverticalfarming.domain.model.VerticalFarmCanopyGrowthNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVerticalFarmCanopyGrowthNodeUseCase {
    VerticalFarmCanopyGrowthNode createVerticalFarmCanopyGrowthNode(String tenantId, String title, double value);
    Optional<VerticalFarmCanopyGrowthNode> findVerticalFarmCanopyGrowthNodeById(String id, String tenantId);
    VerticalFarmCanopyGrowthNode processOptimization(String id, String tenantId);
}
