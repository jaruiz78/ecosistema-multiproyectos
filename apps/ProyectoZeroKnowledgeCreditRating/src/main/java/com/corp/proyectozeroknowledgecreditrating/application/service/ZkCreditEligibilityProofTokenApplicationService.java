package com.corp.proyectozeroknowledgecreditrating.application.service;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import com.corp.proyectozeroknowledgecreditrating.domain.port.in.ManageZkCreditEligibilityProofTokenUseCase;
import com.corp.proyectozeroknowledgecreditrating.domain.port.out.ZkCreditEligibilityProofTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ZkCreditEligibilityProofToken.
 */
@Service
public class ZkCreditEligibilityProofTokenApplicationService implements ManageZkCreditEligibilityProofTokenUseCase {

    private final ZkCreditEligibilityProofTokenRepositoryPort repositoryPort;

    public ZkCreditEligibilityProofTokenApplicationService(ZkCreditEligibilityProofTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ZkCreditEligibilityProofToken createZkCreditEligibilityProofToken(String tenantId, String title, double value) {
        ZkCreditEligibilityProofToken entity = new ZkCreditEligibilityProofToken(
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
    public Optional<ZkCreditEligibilityProofToken> findZkCreditEligibilityProofTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ZkCreditEligibilityProofToken processOptimization(String id, String tenantId) {
        ZkCreditEligibilityProofToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ZkCreditEligibilityProofToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
