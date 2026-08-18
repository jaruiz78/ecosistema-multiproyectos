package com.corp.proyectotokenizedcarbonsatellitemrv.application.service;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.in.ManageVerifiedCarbonSequestrationCreditTokenUseCase;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.out.VerifiedCarbonSequestrationCreditTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VerifiedCarbonSequestrationCreditToken.
 */
@Service
public class VerifiedCarbonSequestrationCreditTokenApplicationService implements ManageVerifiedCarbonSequestrationCreditTokenUseCase {

    private final VerifiedCarbonSequestrationCreditTokenRepositoryPort repositoryPort;

    public VerifiedCarbonSequestrationCreditTokenApplicationService(VerifiedCarbonSequestrationCreditTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VerifiedCarbonSequestrationCreditToken createVerifiedCarbonSequestrationCreditToken(String tenantId, String title, double value) {
        VerifiedCarbonSequestrationCreditToken entity = new VerifiedCarbonSequestrationCreditToken(
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
    public Optional<VerifiedCarbonSequestrationCreditToken> findVerifiedCarbonSequestrationCreditTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VerifiedCarbonSequestrationCreditToken processOptimization(String id, String tenantId) {
        VerifiedCarbonSequestrationCreditToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VerifiedCarbonSequestrationCreditToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
