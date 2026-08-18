package com.corp.proyectodiscretevariableqkdmesh.application.service;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.in.ManageDvQkdDecoyStateKeyStreamTokenUseCase;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.out.DvQkdDecoyStateKeyStreamTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DvQkdDecoyStateKeyStreamToken.
 */
@Service
public class DvQkdDecoyStateKeyStreamTokenApplicationService implements ManageDvQkdDecoyStateKeyStreamTokenUseCase {

    private final DvQkdDecoyStateKeyStreamTokenRepositoryPort repositoryPort;

    public DvQkdDecoyStateKeyStreamTokenApplicationService(DvQkdDecoyStateKeyStreamTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DvQkdDecoyStateKeyStreamToken createDvQkdDecoyStateKeyStreamToken(String tenantId, String title, double value) {
        DvQkdDecoyStateKeyStreamToken entity = new DvQkdDecoyStateKeyStreamToken(
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
    public Optional<DvQkdDecoyStateKeyStreamToken> findDvQkdDecoyStateKeyStreamTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DvQkdDecoyStateKeyStreamToken processOptimization(String id, String tenantId) {
        DvQkdDecoyStateKeyStreamToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DvQkdDecoyStateKeyStreamToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
