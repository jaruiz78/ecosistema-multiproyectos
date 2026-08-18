package com.corp.proyectoorbitalrefuelingcryostation.application.service;

import com.corp.proyectoorbitalrefuelingcryostation.domain.model.CryogenicTransferMassBoiloffToken;
import com.corp.proyectoorbitalrefuelingcryostation.domain.port.in.ManageCryogenicTransferMassBoiloffTokenUseCase;
import com.corp.proyectoorbitalrefuelingcryostation.domain.port.out.CryogenicTransferMassBoiloffTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CryogenicTransferMassBoiloffToken.
 */
@Service
public class CryogenicTransferMassBoiloffTokenApplicationService implements ManageCryogenicTransferMassBoiloffTokenUseCase {

    private final CryogenicTransferMassBoiloffTokenRepositoryPort repositoryPort;

    public CryogenicTransferMassBoiloffTokenApplicationService(CryogenicTransferMassBoiloffTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CryogenicTransferMassBoiloffToken createCryogenicTransferMassBoiloffToken(String tenantId, String title, double value) {
        CryogenicTransferMassBoiloffToken entity = new CryogenicTransferMassBoiloffToken(
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
    public Optional<CryogenicTransferMassBoiloffToken> findCryogenicTransferMassBoiloffTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CryogenicTransferMassBoiloffToken processOptimization(String id, String tenantId) {
        CryogenicTransferMassBoiloffToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CryogenicTransferMassBoiloffToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
