package com.corp.proyectomemristoranalogcompute.domain.port.out;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MemristorCrossbarSynapseNodeRepositoryPort {
    MemristorCrossbarSynapseNode save(MemristorCrossbarSynapseNode entity);
    Optional<MemristorCrossbarSynapseNode> findById(String id, String tenantId);
}
