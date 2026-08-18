package com.corp.proyectolunaroxygenisruplant.application.service;

import com.corp.proyectolunaroxygenisruplant.domain.model.RegolithOxygenExtractionRateYieldToken;
import com.corp.proyectolunaroxygenisruplant.domain.port.in.ManageRegolithOxygenExtractionRateYieldTokenUseCase;
import com.corp.proyectolunaroxygenisruplant.domain.port.out.RegolithOxygenExtractionRateYieldTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de RegolithOxygenExtractionRateYieldToken.
 */
@Service
public class RegolithOxygenExtractionRateYieldTokenApplicationService implements ManageRegolithOxygenExtractionRateYieldTokenUseCase {

    private final RegolithOxygenExtractionRateYieldTokenRepositoryPort repositoryPort;

    public RegolithOxygenExtractionRateYieldTokenApplicationService(RegolithOxygenExtractionRateYieldTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RegolithOxygenExtractionRateYieldToken createRegolithOxygenExtractionRateYieldToken(String tenantId, String title, double value) {
        RegolithOxygenExtractionRateYieldToken entity = new RegolithOxygenExtractionRateYieldToken(
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
    public Optional<RegolithOxygenExtractionRateYieldToken> findRegolithOxygenExtractionRateYieldTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RegolithOxygenExtractionRateYieldToken processOptimization(String id, String tenantId) {
        RegolithOxygenExtractionRateYieldToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RegolithOxygenExtractionRateYieldToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
