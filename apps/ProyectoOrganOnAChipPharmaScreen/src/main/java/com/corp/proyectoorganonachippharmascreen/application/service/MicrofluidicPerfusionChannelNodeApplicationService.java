package com.corp.proyectoorganonachippharmascreen.application.service;

import com.corp.proyectoorganonachippharmascreen.domain.model.MicrofluidicPerfusionChannelNode;
import com.corp.proyectoorganonachippharmascreen.domain.port.in.ManageMicrofluidicPerfusionChannelNodeUseCase;
import com.corp.proyectoorganonachippharmascreen.domain.port.out.MicrofluidicPerfusionChannelNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MicrofluidicPerfusionChannelNode.
 */
@Service
public class MicrofluidicPerfusionChannelNodeApplicationService implements ManageMicrofluidicPerfusionChannelNodeUseCase {

    private final MicrofluidicPerfusionChannelNodeRepositoryPort repositoryPort;

    public MicrofluidicPerfusionChannelNodeApplicationService(MicrofluidicPerfusionChannelNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MicrofluidicPerfusionChannelNode createMicrofluidicPerfusionChannelNode(String tenantId, String title, double value) {
        MicrofluidicPerfusionChannelNode entity = new MicrofluidicPerfusionChannelNode(
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
    public Optional<MicrofluidicPerfusionChannelNode> findMicrofluidicPerfusionChannelNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MicrofluidicPerfusionChannelNode processOptimization(String id, String tenantId) {
        MicrofluidicPerfusionChannelNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MicrofluidicPerfusionChannelNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
