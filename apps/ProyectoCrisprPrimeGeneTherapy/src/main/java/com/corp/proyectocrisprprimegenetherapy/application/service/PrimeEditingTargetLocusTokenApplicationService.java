package com.corp.proyectocrisprprimegenetherapy.application.service;

import com.corp.proyectocrisprprimegenetherapy.domain.model.PrimeEditingTargetLocusToken;
import com.corp.proyectocrisprprimegenetherapy.domain.port.in.ManagePrimeEditingTargetLocusTokenUseCase;
import com.corp.proyectocrisprprimegenetherapy.domain.port.out.PrimeEditingTargetLocusTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PrimeEditingTargetLocusToken.
 */
@Service
public class PrimeEditingTargetLocusTokenApplicationService implements ManagePrimeEditingTargetLocusTokenUseCase {

    private final PrimeEditingTargetLocusTokenRepositoryPort repositoryPort;

    public PrimeEditingTargetLocusTokenApplicationService(PrimeEditingTargetLocusTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PrimeEditingTargetLocusToken createPrimeEditingTargetLocusToken(String tenantId, String title, double value) {
        PrimeEditingTargetLocusToken entity = new PrimeEditingTargetLocusToken(
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
    public Optional<PrimeEditingTargetLocusToken> findPrimeEditingTargetLocusTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PrimeEditingTargetLocusToken processOptimization(String id, String tenantId) {
        PrimeEditingTargetLocusToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PrimeEditingTargetLocusToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
