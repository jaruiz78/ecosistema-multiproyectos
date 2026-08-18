package com.corp.proyectoprogrammableofflinecbdc.application.service;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.in.ManageOfflineCbdcSpendProofTokenUseCase;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.out.OfflineCbdcSpendProofTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de OfflineCbdcSpendProofToken.
 */
@Service
public class OfflineCbdcSpendProofTokenApplicationService implements ManageOfflineCbdcSpendProofTokenUseCase {

    private final OfflineCbdcSpendProofTokenRepositoryPort repositoryPort;

    public OfflineCbdcSpendProofTokenApplicationService(OfflineCbdcSpendProofTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public OfflineCbdcSpendProofToken createOfflineCbdcSpendProofToken(String tenantId, String title, double value) {
        OfflineCbdcSpendProofToken entity = new OfflineCbdcSpendProofToken(
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
    public Optional<OfflineCbdcSpendProofToken> findOfflineCbdcSpendProofTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public OfflineCbdcSpendProofToken processOptimization(String id, String tenantId) {
        OfflineCbdcSpendProofToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        OfflineCbdcSpendProofToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
