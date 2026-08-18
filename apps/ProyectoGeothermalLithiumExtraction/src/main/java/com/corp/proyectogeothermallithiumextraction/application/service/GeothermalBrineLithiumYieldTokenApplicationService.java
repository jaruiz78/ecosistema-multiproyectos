package com.corp.proyectogeothermallithiumextraction.application.service;

import com.corp.proyectogeothermallithiumextraction.domain.model.GeothermalBrineLithiumYieldToken;
import com.corp.proyectogeothermallithiumextraction.domain.port.in.ManageGeothermalBrineLithiumYieldTokenUseCase;
import com.corp.proyectogeothermallithiumextraction.domain.port.out.GeothermalBrineLithiumYieldTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GeothermalBrineLithiumYieldToken.
 */
@Service
public class GeothermalBrineLithiumYieldTokenApplicationService implements ManageGeothermalBrineLithiumYieldTokenUseCase {

    private final GeothermalBrineLithiumYieldTokenRepositoryPort repositoryPort;

    public GeothermalBrineLithiumYieldTokenApplicationService(GeothermalBrineLithiumYieldTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GeothermalBrineLithiumYieldToken createGeothermalBrineLithiumYieldToken(String tenantId, String title, double value) {
        GeothermalBrineLithiumYieldToken entity = new GeothermalBrineLithiumYieldToken(
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
    public Optional<GeothermalBrineLithiumYieldToken> findGeothermalBrineLithiumYieldTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GeothermalBrineLithiumYieldToken processOptimization(String id, String tenantId) {
        GeothermalBrineLithiumYieldToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GeothermalBrineLithiumYieldToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
