package com.corp.proyectoredparadorestwin.application.service;

import com.corp.proyectoredparadorestwin.domain.model.ParadorRoomNightStay;
import com.corp.proyectoredparadorestwin.domain.port.in.ManageParadorRoomNightStayUseCase;
import com.corp.proyectoredparadorestwin.domain.port.out.ParadorRoomNightStayRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ParadorRoomNightStay.
 */
@Service
public class ParadorRoomNightStayApplicationService implements ManageParadorRoomNightStayUseCase {

    private final ParadorRoomNightStayRepositoryPort repositoryPort;

    public ParadorRoomNightStayApplicationService(ParadorRoomNightStayRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ParadorRoomNightStay createParadorRoomNightStay(String tenantId, String title, double value) {
        ParadorRoomNightStay entity = new ParadorRoomNightStay(
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
    public Optional<ParadorRoomNightStay> findParadorRoomNightStayById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ParadorRoomNightStay processOptimization(String id, String tenantId) {
        ParadorRoomNightStay existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ParadorRoomNightStay optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
