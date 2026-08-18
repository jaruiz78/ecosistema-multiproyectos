package com.corp.proyectoomniplanetarymastertwin.application.service;

import com.corp.proyectoomniplanetarymastertwin.domain.model.OmniPlanetaryTensorGraphNexusNode;
import com.corp.proyectoomniplanetarymastertwin.domain.port.in.ManageOmniPlanetaryTensorGraphNexusNodeUseCase;
import com.corp.proyectoomniplanetarymastertwin.domain.port.out.OmniPlanetaryTensorGraphNexusNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de OmniPlanetaryTensorGraphNexusNode.
 */
@Service
public class OmniPlanetaryTensorGraphNexusNodeApplicationService implements ManageOmniPlanetaryTensorGraphNexusNodeUseCase {

    private final OmniPlanetaryTensorGraphNexusNodeRepositoryPort repositoryPort;

    public OmniPlanetaryTensorGraphNexusNodeApplicationService(OmniPlanetaryTensorGraphNexusNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public OmniPlanetaryTensorGraphNexusNode createOmniPlanetaryTensorGraphNexusNode(String tenantId, String title, double value) {
        OmniPlanetaryTensorGraphNexusNode entity = new OmniPlanetaryTensorGraphNexusNode(
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
    public Optional<OmniPlanetaryTensorGraphNexusNode> findOmniPlanetaryTensorGraphNexusNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public OmniPlanetaryTensorGraphNexusNode processOptimization(String id, String tenantId) {
        OmniPlanetaryTensorGraphNexusNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        OmniPlanetaryTensorGraphNexusNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
