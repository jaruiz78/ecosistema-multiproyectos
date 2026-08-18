package com.corp.proyectoicecorepaleoclimatearchive.application.service;

import com.corp.proyectoicecorepaleoclimatearchive.domain.model.IsotopicDeltaO18TemperatureProfileToken;
import com.corp.proyectoicecorepaleoclimatearchive.domain.port.in.ManageIsotopicDeltaO18TemperatureProfileTokenUseCase;
import com.corp.proyectoicecorepaleoclimatearchive.domain.port.out.IsotopicDeltaO18TemperatureProfileTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de IsotopicDeltaO18TemperatureProfileToken.
 */
@Service
public class IsotopicDeltaO18TemperatureProfileTokenApplicationService implements ManageIsotopicDeltaO18TemperatureProfileTokenUseCase {

    private final IsotopicDeltaO18TemperatureProfileTokenRepositoryPort repositoryPort;

    public IsotopicDeltaO18TemperatureProfileTokenApplicationService(IsotopicDeltaO18TemperatureProfileTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public IsotopicDeltaO18TemperatureProfileToken createIsotopicDeltaO18TemperatureProfileToken(String tenantId, String title, double value) {
        IsotopicDeltaO18TemperatureProfileToken entity = new IsotopicDeltaO18TemperatureProfileToken(
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
    public Optional<IsotopicDeltaO18TemperatureProfileToken> findIsotopicDeltaO18TemperatureProfileTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public IsotopicDeltaO18TemperatureProfileToken processOptimization(String id, String tenantId) {
        IsotopicDeltaO18TemperatureProfileToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        IsotopicDeltaO18TemperatureProfileToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
