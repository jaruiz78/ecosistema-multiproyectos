package com.corp.proyectoevtolurbanaircorridors.application.service;

import com.corp.proyectoevtolurbanaircorridors.domain.model.EvtolFlightTrajectorySlotToken;
import com.corp.proyectoevtolurbanaircorridors.domain.port.in.ManageEvtolFlightTrajectorySlotTokenUseCase;
import com.corp.proyectoevtolurbanaircorridors.domain.port.out.EvtolFlightTrajectorySlotTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EvtolFlightTrajectorySlotToken.
 */
@Service
public class EvtolFlightTrajectorySlotTokenApplicationService implements ManageEvtolFlightTrajectorySlotTokenUseCase {

    private final EvtolFlightTrajectorySlotTokenRepositoryPort repositoryPort;

    public EvtolFlightTrajectorySlotTokenApplicationService(EvtolFlightTrajectorySlotTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EvtolFlightTrajectorySlotToken createEvtolFlightTrajectorySlotToken(String tenantId, String title, double value) {
        EvtolFlightTrajectorySlotToken entity = new EvtolFlightTrajectorySlotToken(
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
    public Optional<EvtolFlightTrajectorySlotToken> findEvtolFlightTrajectorySlotTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EvtolFlightTrajectorySlotToken processOptimization(String id, String tenantId) {
        EvtolFlightTrajectorySlotToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EvtolFlightTrajectorySlotToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
