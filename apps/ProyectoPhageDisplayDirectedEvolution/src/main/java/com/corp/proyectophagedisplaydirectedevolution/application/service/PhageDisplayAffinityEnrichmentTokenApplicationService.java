package com.corp.proyectophagedisplaydirectedevolution.application.service;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import com.corp.proyectophagedisplaydirectedevolution.domain.port.in.ManagePhageDisplayAffinityEnrichmentTokenUseCase;
import com.corp.proyectophagedisplaydirectedevolution.domain.port.out.PhageDisplayAffinityEnrichmentTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PhageDisplayAffinityEnrichmentToken.
 */
@Service
public class PhageDisplayAffinityEnrichmentTokenApplicationService implements ManagePhageDisplayAffinityEnrichmentTokenUseCase {

    private final PhageDisplayAffinityEnrichmentTokenRepositoryPort repositoryPort;

    public PhageDisplayAffinityEnrichmentTokenApplicationService(PhageDisplayAffinityEnrichmentTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PhageDisplayAffinityEnrichmentToken createPhageDisplayAffinityEnrichmentToken(String tenantId, String title, double value) {
        PhageDisplayAffinityEnrichmentToken entity = new PhageDisplayAffinityEnrichmentToken(
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
    public Optional<PhageDisplayAffinityEnrichmentToken> findPhageDisplayAffinityEnrichmentTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PhageDisplayAffinityEnrichmentToken processOptimization(String id, String tenantId) {
        PhageDisplayAffinityEnrichmentToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PhageDisplayAffinityEnrichmentToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
