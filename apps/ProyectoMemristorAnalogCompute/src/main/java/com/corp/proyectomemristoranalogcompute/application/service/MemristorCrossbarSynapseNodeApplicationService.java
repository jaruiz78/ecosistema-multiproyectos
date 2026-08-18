package com.corp.proyectomemristoranalogcompute.application.service;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import com.corp.proyectomemristoranalogcompute.domain.port.in.ManageMemristorCrossbarSynapseNodeUseCase;
import com.corp.proyectomemristoranalogcompute.domain.port.out.MemristorCrossbarSynapseNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MemristorCrossbarSynapseNode.
 */
@Service
public class MemristorCrossbarSynapseNodeApplicationService implements ManageMemristorCrossbarSynapseNodeUseCase {

    private final MemristorCrossbarSynapseNodeRepositoryPort repositoryPort;

    public MemristorCrossbarSynapseNodeApplicationService(MemristorCrossbarSynapseNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MemristorCrossbarSynapseNode createMemristorCrossbarSynapseNode(String tenantId, String title, double value) {
        MemristorCrossbarSynapseNode entity = new MemristorCrossbarSynapseNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<MemristorCrossbarSynapseNode> findMemristorCrossbarSynapseNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MemristorCrossbarSynapseNode processOptimization(String id, String tenantId) {
        MemristorCrossbarSynapseNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MemristorCrossbarSynapseNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
