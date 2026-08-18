package com.corp.proyectomycorrhizalnetworkagronomy.domain.port.in;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHyphalNutrientFluxTranslocationNodeUseCase {
    HyphalNutrientFluxTranslocationNode createHyphalNutrientFluxTranslocationNode(String tenantId, String title, double value);
    Optional<HyphalNutrientFluxTranslocationNode> findHyphalNutrientFluxTranslocationNodeById(String id, String tenantId);
    HyphalNutrientFluxTranslocationNode processOptimization(String id, String tenantId);
}
