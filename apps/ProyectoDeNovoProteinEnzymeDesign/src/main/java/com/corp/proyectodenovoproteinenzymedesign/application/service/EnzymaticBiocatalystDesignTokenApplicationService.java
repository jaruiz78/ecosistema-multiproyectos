package com.corp.proyectodenovoproteinenzymedesign.application.service;

import com.corp.proyectodenovoproteinenzymedesign.domain.model.EnzymaticBiocatalystDesignToken;
import com.corp.proyectodenovoproteinenzymedesign.domain.port.in.ManageEnzymaticBiocatalystDesignTokenUseCase;
import com.corp.proyectodenovoproteinenzymedesign.domain.port.out.EnzymaticBiocatalystDesignTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EnzymaticBiocatalystDesignToken.
 */
@Service
public class EnzymaticBiocatalystDesignTokenApplicationService implements ManageEnzymaticBiocatalystDesignTokenUseCase {

    private final EnzymaticBiocatalystDesignTokenRepositoryPort repositoryPort;

    public EnzymaticBiocatalystDesignTokenApplicationService(EnzymaticBiocatalystDesignTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EnzymaticBiocatalystDesignToken createEnzymaticBiocatalystDesignToken(String tenantId, String title, double value) {
        EnzymaticBiocatalystDesignToken entity = new EnzymaticBiocatalystDesignToken(
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
    public Optional<EnzymaticBiocatalystDesignToken> findEnzymaticBiocatalystDesignTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EnzymaticBiocatalystDesignToken processOptimization(String id, String tenantId) {
        EnzymaticBiocatalystDesignToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EnzymaticBiocatalystDesignToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
