package com.corp.proyectoneuromorphicedgesnn.domain.port.in;

import com.corp.proyectoneuromorphicedgesnn.domain.model.NeuromorphicSpikeEventNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageNeuromorphicSpikeEventNodeUseCase {
    NeuromorphicSpikeEventNode createNeuromorphicSpikeEventNode(String tenantId, String title, double value);
    Optional<NeuromorphicSpikeEventNode> findNeuromorphicSpikeEventNodeById(String id, String tenantId);
    NeuromorphicSpikeEventNode processOptimization(String id, String tenantId);
}
