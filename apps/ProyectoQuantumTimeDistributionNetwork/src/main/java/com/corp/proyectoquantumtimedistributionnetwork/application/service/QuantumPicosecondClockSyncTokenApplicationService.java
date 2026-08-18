package com.corp.proyectoquantumtimedistributionnetwork.application.service;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.in.ManageQuantumPicosecondClockSyncTokenUseCase;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.out.QuantumPicosecondClockSyncTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuantumPicosecondClockSyncToken.
 */
@Service
public class QuantumPicosecondClockSyncTokenApplicationService implements ManageQuantumPicosecondClockSyncTokenUseCase {

    private final QuantumPicosecondClockSyncTokenRepositoryPort repositoryPort;

    public QuantumPicosecondClockSyncTokenApplicationService(QuantumPicosecondClockSyncTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumPicosecondClockSyncToken createQuantumPicosecondClockSyncToken(String tenantId, String title, double value) {
        QuantumPicosecondClockSyncToken entity = new QuantumPicosecondClockSyncToken(
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
    public Optional<QuantumPicosecondClockSyncToken> findQuantumPicosecondClockSyncTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumPicosecondClockSyncToken processOptimization(String id, String tenantId) {
        QuantumPicosecondClockSyncToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumPicosecondClockSyncToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
