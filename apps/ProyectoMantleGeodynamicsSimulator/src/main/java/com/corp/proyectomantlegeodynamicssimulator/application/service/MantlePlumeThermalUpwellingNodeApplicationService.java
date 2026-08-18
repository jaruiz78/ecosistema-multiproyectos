package com.corp.proyectomantlegeodynamicssimulator.application.service;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.in.ManageMantlePlumeThermalUpwellingNodeUseCase;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.out.MantlePlumeThermalUpwellingNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MantlePlumeThermalUpwellingNode.
 */
@Service
public class MantlePlumeThermalUpwellingNodeApplicationService implements ManageMantlePlumeThermalUpwellingNodeUseCase {

    private final MantlePlumeThermalUpwellingNodeRepositoryPort repositoryPort;

    public MantlePlumeThermalUpwellingNodeApplicationService(MantlePlumeThermalUpwellingNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MantlePlumeThermalUpwellingNode createMantlePlumeThermalUpwellingNode(String tenantId, String title, double value) {
        MantlePlumeThermalUpwellingNode entity = new MantlePlumeThermalUpwellingNode(
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
    public Optional<MantlePlumeThermalUpwellingNode> findMantlePlumeThermalUpwellingNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MantlePlumeThermalUpwellingNode processOptimization(String id, String tenantId) {
        MantlePlumeThermalUpwellingNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MantlePlumeThermalUpwellingNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
