package com.corp.proyectomemristoranalogcompute.domain.port.in;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMemristorCrossbarSynapseNodeUseCase {
    MemristorCrossbarSynapseNode createMemristorCrossbarSynapseNode(String tenantId, String title, double value);
    Optional<MemristorCrossbarSynapseNode> findMemristorCrossbarSynapseNodeById(String id, String tenantId);
    MemristorCrossbarSynapseNode processOptimization(String id, String tenantId);
}
