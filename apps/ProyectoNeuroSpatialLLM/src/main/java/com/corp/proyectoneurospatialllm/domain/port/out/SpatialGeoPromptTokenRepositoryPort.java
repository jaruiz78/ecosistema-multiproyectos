package com.corp.proyectoneurospatialllm.domain.port.out;

import com.corp.proyectoneurospatialllm.domain.model.SpatialGeoPromptToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpatialGeoPromptTokenRepositoryPort {
    SpatialGeoPromptToken save(SpatialGeoPromptToken entity);
    Optional<SpatialGeoPromptToken> findById(String id, String tenantId);
}
