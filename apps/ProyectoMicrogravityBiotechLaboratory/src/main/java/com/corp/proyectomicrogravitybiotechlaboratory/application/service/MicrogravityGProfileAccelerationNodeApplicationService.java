package com.corp.proyectomicrogravitybiotechlaboratory.application.service;

import com.corp.proyectomicrogravitybiotechlaboratory.domain.model.MicrogravityGProfileAccelerationNode;
import com.corp.proyectomicrogravitybiotechlaboratory.domain.port.in.ManageMicrogravityGProfileAccelerationNodeUseCase;
import com.corp.proyectomicrogravitybiotechlaboratory.domain.port.out.MicrogravityGProfileAccelerationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MicrogravityGProfileAccelerationNode.
 */
@Service
public class MicrogravityGProfileAccelerationNodeApplicationService implements ManageMicrogravityGProfileAccelerationNodeUseCase {

    private final MicrogravityGProfileAccelerationNodeRepositoryPort repositoryPort;

    public MicrogravityGProfileAccelerationNodeApplicationService(MicrogravityGProfileAccelerationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MicrogravityGProfileAccelerationNode createMicrogravityGProfileAccelerationNode(String tenantId, String title, double value) {
        MicrogravityGProfileAccelerationNode entity = new MicrogravityGProfileAccelerationNode(
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
    public Optional<MicrogravityGProfileAccelerationNode> findMicrogravityGProfileAccelerationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MicrogravityGProfileAccelerationNode processOptimization(String id, String tenantId) {
        MicrogravityGProfileAccelerationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MicrogravityGProfileAccelerationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
