package com.corp.proyectoeidas2digitalidentitywallet.application.service;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import com.corp.proyectoeidas2digitalidentitywallet.domain.port.in.ManageVerifiableCredentialStatusListTokenUseCase;
import com.corp.proyectoeidas2digitalidentitywallet.domain.port.out.VerifiableCredentialStatusListTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VerifiableCredentialStatusListToken.
 */
@Service
public class VerifiableCredentialStatusListTokenApplicationService implements ManageVerifiableCredentialStatusListTokenUseCase {

    private final VerifiableCredentialStatusListTokenRepositoryPort repositoryPort;

    public VerifiableCredentialStatusListTokenApplicationService(VerifiableCredentialStatusListTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VerifiableCredentialStatusListToken createVerifiableCredentialStatusListToken(String tenantId, String title, double value) {
        VerifiableCredentialStatusListToken entity = new VerifiableCredentialStatusListToken(
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
    public Optional<VerifiableCredentialStatusListToken> findVerifiableCredentialStatusListTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VerifiableCredentialStatusListToken processOptimization(String id, String tenantId) {
        VerifiableCredentialStatusListToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VerifiableCredentialStatusListToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
