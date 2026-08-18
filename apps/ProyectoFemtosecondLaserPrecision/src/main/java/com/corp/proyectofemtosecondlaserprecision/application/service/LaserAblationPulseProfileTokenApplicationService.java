package com.corp.proyectofemtosecondlaserprecision.application.service;

import com.corp.proyectofemtosecondlaserprecision.domain.model.LaserAblationPulseProfileToken;
import com.corp.proyectofemtosecondlaserprecision.domain.port.in.ManageLaserAblationPulseProfileTokenUseCase;
import com.corp.proyectofemtosecondlaserprecision.domain.port.out.LaserAblationPulseProfileTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LaserAblationPulseProfileToken.
 */
@Service
public class LaserAblationPulseProfileTokenApplicationService implements ManageLaserAblationPulseProfileTokenUseCase {

    private final LaserAblationPulseProfileTokenRepositoryPort repositoryPort;

    public LaserAblationPulseProfileTokenApplicationService(LaserAblationPulseProfileTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LaserAblationPulseProfileToken createLaserAblationPulseProfileToken(String tenantId, String title, double value) {
        LaserAblationPulseProfileToken entity = new LaserAblationPulseProfileToken(
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
    public Optional<LaserAblationPulseProfileToken> findLaserAblationPulseProfileTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LaserAblationPulseProfileToken processOptimization(String id, String tenantId) {
        LaserAblationPulseProfileToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LaserAblationPulseProfileToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
