package com.corp.proyectoneuromorphicedgesnn.application.service;

import com.corp.proyectoneuromorphicedgesnn.domain.model.NeuromorphicSpikeEventNode;
import com.corp.proyectoneuromorphicedgesnn.domain.port.in.ManageNeuromorphicSpikeEventNodeUseCase;
import com.corp.proyectoneuromorphicedgesnn.domain.port.out.NeuromorphicSpikeEventNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de NeuromorphicSpikeEventNode.
 */
@Service
public class NeuromorphicSpikeEventNodeApplicationService implements ManageNeuromorphicSpikeEventNodeUseCase {

    private final NeuromorphicSpikeEventNodeRepositoryPort repositoryPort;

    public NeuromorphicSpikeEventNodeApplicationService(NeuromorphicSpikeEventNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public NeuromorphicSpikeEventNode createNeuromorphicSpikeEventNode(String tenantId, String title, double value) {
        NeuromorphicSpikeEventNode entity = new NeuromorphicSpikeEventNode(
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
    public Optional<NeuromorphicSpikeEventNode> findNeuromorphicSpikeEventNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public NeuromorphicSpikeEventNode processOptimization(String id, String tenantId) {
        NeuromorphicSpikeEventNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        NeuromorphicSpikeEventNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
