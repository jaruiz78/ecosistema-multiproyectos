package com.corp.proyectoneurospatialllm.domain.port.in;

import com.corp.proyectoneurospatialllm.domain.model.SpatialGeoPromptToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpatialGeoPromptTokenUseCase {
    SpatialGeoPromptToken createSpatialGeoPromptToken(String tenantId, String title, double value);
    Optional<SpatialGeoPromptToken> findSpatialGeoPromptTokenById(String id, String tenantId);
    SpatialGeoPromptToken processOptimization(String id, String tenantId);
}
