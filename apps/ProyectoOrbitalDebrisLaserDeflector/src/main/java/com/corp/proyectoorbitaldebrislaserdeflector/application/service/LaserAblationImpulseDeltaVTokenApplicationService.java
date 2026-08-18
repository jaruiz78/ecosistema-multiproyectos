package com.corp.proyectoorbitaldebrislaserdeflector.application.service;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import com.corp.proyectoorbitaldebrislaserdeflector.domain.port.in.ManageLaserAblationImpulseDeltaVTokenUseCase;
import com.corp.proyectoorbitaldebrislaserdeflector.domain.port.out.LaserAblationImpulseDeltaVTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LaserAblationImpulseDeltaVToken.
 */
@Service
public class LaserAblationImpulseDeltaVTokenApplicationService implements ManageLaserAblationImpulseDeltaVTokenUseCase {

    private final LaserAblationImpulseDeltaVTokenRepositoryPort repositoryPort;

    public LaserAblationImpulseDeltaVTokenApplicationService(LaserAblationImpulseDeltaVTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LaserAblationImpulseDeltaVToken createLaserAblationImpulseDeltaVToken(String tenantId, String title, double value) {
        LaserAblationImpulseDeltaVToken entity = new LaserAblationImpulseDeltaVToken(
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
    public Optional<LaserAblationImpulseDeltaVToken> findLaserAblationImpulseDeltaVTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LaserAblationImpulseDeltaVToken processOptimization(String id, String tenantId) {
        LaserAblationImpulseDeltaVToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LaserAblationImpulseDeltaVToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
