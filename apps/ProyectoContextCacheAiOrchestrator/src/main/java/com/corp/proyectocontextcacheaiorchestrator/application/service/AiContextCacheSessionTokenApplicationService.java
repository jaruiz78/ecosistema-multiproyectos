package com.corp.proyectocontextcacheaiorchestrator.application.service;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.in.ManageAiContextCacheSessionTokenUseCase;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.out.AiContextCacheSessionTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AiContextCacheSessionToken.
 */
@Service
public class AiContextCacheSessionTokenApplicationService implements ManageAiContextCacheSessionTokenUseCase {

    private final AiContextCacheSessionTokenRepositoryPort repositoryPort;

    public AiContextCacheSessionTokenApplicationService(AiContextCacheSessionTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AiContextCacheSessionToken createAiContextCacheSessionToken(String tenantId, String title, double value) {
        AiContextCacheSessionToken entity = new AiContextCacheSessionToken(
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
    public Optional<AiContextCacheSessionToken> findAiContextCacheSessionTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AiContextCacheSessionToken processOptimization(String id, String tenantId) {
        AiContextCacheSessionToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AiContextCacheSessionToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
