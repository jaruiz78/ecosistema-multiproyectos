package com.corp.proyectoneuromorphicedgesnn.domain.port.out;

import com.corp.proyectoneuromorphicedgesnn.domain.model.NeuromorphicSpikeEventNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface NeuromorphicSpikeEventNodeRepositoryPort {
    NeuromorphicSpikeEventNode save(NeuromorphicSpikeEventNode entity);
    Optional<NeuromorphicSpikeEventNode> findById(String id, String tenantId);
}
