package com.corp.proyectomycorrhizalnetworkagronomy.domain.port.out;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HyphalNutrientFluxTranslocationNodeRepositoryPort {
    HyphalNutrientFluxTranslocationNode save(HyphalNutrientFluxTranslocationNode entity);
    Optional<HyphalNutrientFluxTranslocationNode> findById(String id, String tenantId);
}
