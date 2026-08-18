package com.corp.proyectothresholdblsoraclenetwork.application.service;

import com.corp.proyectothresholdblsoraclenetwork.domain.model.BlsAggregatedSignatureDataFeedToken;
import com.corp.proyectothresholdblsoraclenetwork.domain.port.in.ManageBlsAggregatedSignatureDataFeedTokenUseCase;
import com.corp.proyectothresholdblsoraclenetwork.domain.port.out.BlsAggregatedSignatureDataFeedTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BlsAggregatedSignatureDataFeedToken.
 */
@Service
public class BlsAggregatedSignatureDataFeedTokenApplicationService implements ManageBlsAggregatedSignatureDataFeedTokenUseCase {

    private final BlsAggregatedSignatureDataFeedTokenRepositoryPort repositoryPort;

    public BlsAggregatedSignatureDataFeedTokenApplicationService(BlsAggregatedSignatureDataFeedTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BlsAggregatedSignatureDataFeedToken createBlsAggregatedSignatureDataFeedToken(String tenantId, String title, double value) {
        BlsAggregatedSignatureDataFeedToken entity = new BlsAggregatedSignatureDataFeedToken(
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
    public Optional<BlsAggregatedSignatureDataFeedToken> findBlsAggregatedSignatureDataFeedTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BlsAggregatedSignatureDataFeedToken processOptimization(String id, String tenantId) {
        BlsAggregatedSignatureDataFeedToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BlsAggregatedSignatureDataFeedToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
