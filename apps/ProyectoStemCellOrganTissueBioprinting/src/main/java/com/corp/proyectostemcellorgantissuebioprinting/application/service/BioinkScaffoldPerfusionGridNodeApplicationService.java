package com.corp.proyectostemcellorgantissuebioprinting.application.service;

import com.corp.proyectostemcellorgantissuebioprinting.domain.model.BioinkScaffoldPerfusionGridNode;
import com.corp.proyectostemcellorgantissuebioprinting.domain.port.in.ManageBioinkScaffoldPerfusionGridNodeUseCase;
import com.corp.proyectostemcellorgantissuebioprinting.domain.port.out.BioinkScaffoldPerfusionGridNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BioinkScaffoldPerfusionGridNode.
 */
@Service
public class BioinkScaffoldPerfusionGridNodeApplicationService implements ManageBioinkScaffoldPerfusionGridNodeUseCase {

    private final BioinkScaffoldPerfusionGridNodeRepositoryPort repositoryPort;

    public BioinkScaffoldPerfusionGridNodeApplicationService(BioinkScaffoldPerfusionGridNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BioinkScaffoldPerfusionGridNode createBioinkScaffoldPerfusionGridNode(String tenantId, String title, double value) {
        BioinkScaffoldPerfusionGridNode entity = new BioinkScaffoldPerfusionGridNode(
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
    public Optional<BioinkScaffoldPerfusionGridNode> findBioinkScaffoldPerfusionGridNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BioinkScaffoldPerfusionGridNode processOptimization(String id, String tenantId) {
        BioinkScaffoldPerfusionGridNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BioinkScaffoldPerfusionGridNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
