package com.corp.proyectominimalgenomechassisfoundry.application.service;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import com.corp.proyectominimalgenomechassisfoundry.domain.port.in.ManageEssentialGeneSetCoverageTokenUseCase;
import com.corp.proyectominimalgenomechassisfoundry.domain.port.out.EssentialGeneSetCoverageTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EssentialGeneSetCoverageToken.
 */
@Service
public class EssentialGeneSetCoverageTokenApplicationService implements ManageEssentialGeneSetCoverageTokenUseCase {

    private final EssentialGeneSetCoverageTokenRepositoryPort repositoryPort;

    public EssentialGeneSetCoverageTokenApplicationService(EssentialGeneSetCoverageTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EssentialGeneSetCoverageToken createEssentialGeneSetCoverageToken(String tenantId, String title, double value) {
        EssentialGeneSetCoverageToken entity = new EssentialGeneSetCoverageToken(
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
    public Optional<EssentialGeneSetCoverageToken> findEssentialGeneSetCoverageTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EssentialGeneSetCoverageToken processOptimization(String id, String tenantId) {
        EssentialGeneSetCoverageToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EssentialGeneSetCoverageToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
