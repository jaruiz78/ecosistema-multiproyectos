package com.corp.proyectobasaltcarbonmineralizationdac.application.service;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.in.ManageBasaltCarbonSequestrationWellTokenUseCase;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.out.BasaltCarbonSequestrationWellTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BasaltCarbonSequestrationWellToken.
 */
@Service
public class BasaltCarbonSequestrationWellTokenApplicationService implements ManageBasaltCarbonSequestrationWellTokenUseCase {

    private final BasaltCarbonSequestrationWellTokenRepositoryPort repositoryPort;

    public BasaltCarbonSequestrationWellTokenApplicationService(BasaltCarbonSequestrationWellTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BasaltCarbonSequestrationWellToken createBasaltCarbonSequestrationWellToken(String tenantId, String title, double value) {
        BasaltCarbonSequestrationWellToken entity = new BasaltCarbonSequestrationWellToken(
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
    public Optional<BasaltCarbonSequestrationWellToken> findBasaltCarbonSequestrationWellTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BasaltCarbonSequestrationWellToken processOptimization(String id, String tenantId) {
        BasaltCarbonSequestrationWellToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BasaltCarbonSequestrationWellToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
