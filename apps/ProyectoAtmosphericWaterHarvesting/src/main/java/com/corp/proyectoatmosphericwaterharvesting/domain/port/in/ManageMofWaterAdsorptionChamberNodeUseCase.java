package com.corp.proyectoatmosphericwaterharvesting.domain.port.in;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMofWaterAdsorptionChamberNodeUseCase {
    MofWaterAdsorptionChamberNode createMofWaterAdsorptionChamberNode(String tenantId, String title, double value);
    Optional<MofWaterAdsorptionChamberNode> findMofWaterAdsorptionChamberNodeById(String id, String tenantId);
    MofWaterAdsorptionChamberNode processOptimization(String id, String tenantId);
}
