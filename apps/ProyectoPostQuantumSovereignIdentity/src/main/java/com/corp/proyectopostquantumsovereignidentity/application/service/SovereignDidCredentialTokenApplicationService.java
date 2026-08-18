package com.corp.proyectopostquantumsovereignidentity.application.service;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import com.corp.proyectopostquantumsovereignidentity.domain.port.in.ManageSovereignDidCredentialTokenUseCase;
import com.corp.proyectopostquantumsovereignidentity.domain.port.out.SovereignDidCredentialTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SovereignDidCredentialToken.
 */
@Service
public class SovereignDidCredentialTokenApplicationService implements ManageSovereignDidCredentialTokenUseCase {

    private final SovereignDidCredentialTokenRepositoryPort repositoryPort;

    public SovereignDidCredentialTokenApplicationService(SovereignDidCredentialTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SovereignDidCredentialToken createSovereignDidCredentialToken(String tenantId, String title, double value) {
        SovereignDidCredentialToken entity = new SovereignDidCredentialToken(
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
    public Optional<SovereignDidCredentialToken> findSovereignDidCredentialTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SovereignDidCredentialToken processOptimization(String id, String tenantId) {
        SovereignDidCredentialToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SovereignDidCredentialToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
