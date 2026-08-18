package com.corp.proyectobacteriophageprecisionantimicrobial.application.service;

import com.corp.proyectobacteriophageprecisionantimicrobial.domain.model.EndolysinLyticActivityScoreToken;
import com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.in.ManageEndolysinLyticActivityScoreTokenUseCase;
import com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.out.EndolysinLyticActivityScoreTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EndolysinLyticActivityScoreToken.
 */
@Service
public class EndolysinLyticActivityScoreTokenApplicationService implements ManageEndolysinLyticActivityScoreTokenUseCase {

    private final EndolysinLyticActivityScoreTokenRepositoryPort repositoryPort;

    public EndolysinLyticActivityScoreTokenApplicationService(EndolysinLyticActivityScoreTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EndolysinLyticActivityScoreToken createEndolysinLyticActivityScoreToken(String tenantId, String title, double value) {
        EndolysinLyticActivityScoreToken entity = new EndolysinLyticActivityScoreToken(
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
    public Optional<EndolysinLyticActivityScoreToken> findEndolysinLyticActivityScoreTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EndolysinLyticActivityScoreToken processOptimization(String id, String tenantId) {
        EndolysinLyticActivityScoreToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EndolysinLyticActivityScoreToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
