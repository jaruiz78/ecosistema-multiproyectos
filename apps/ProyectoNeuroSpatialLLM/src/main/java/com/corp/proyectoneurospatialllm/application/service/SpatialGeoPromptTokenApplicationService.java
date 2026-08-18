package com.corp.proyectoneurospatialllm.application.service;

import com.corp.proyectoneurospatialllm.domain.model.SpatialGeoPromptToken;
import com.corp.proyectoneurospatialllm.domain.port.in.ManageSpatialGeoPromptTokenUseCase;
import com.corp.proyectoneurospatialllm.domain.port.out.SpatialGeoPromptTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpatialGeoPromptToken.
 */
@Service
public class SpatialGeoPromptTokenApplicationService implements ManageSpatialGeoPromptTokenUseCase {

    private final SpatialGeoPromptTokenRepositoryPort repositoryPort;

    public SpatialGeoPromptTokenApplicationService(SpatialGeoPromptTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpatialGeoPromptToken createSpatialGeoPromptToken(String tenantId, String title, double value) {
        SpatialGeoPromptToken entity = new SpatialGeoPromptToken(
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
    public Optional<SpatialGeoPromptToken> findSpatialGeoPromptTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpatialGeoPromptToken processOptimization(String id, String tenantId) {
        SpatialGeoPromptToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpatialGeoPromptToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
