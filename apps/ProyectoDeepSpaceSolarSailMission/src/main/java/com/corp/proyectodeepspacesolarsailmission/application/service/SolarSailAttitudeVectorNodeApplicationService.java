package com.corp.proyectodeepspacesolarsailmission.application.service;

import com.corp.proyectodeepspacesolarsailmission.domain.model.SolarSailAttitudeVectorNode;
import com.corp.proyectodeepspacesolarsailmission.domain.port.in.ManageSolarSailAttitudeVectorNodeUseCase;
import com.corp.proyectodeepspacesolarsailmission.domain.port.out.SolarSailAttitudeVectorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SolarSailAttitudeVectorNode.
 */
@Service
public class SolarSailAttitudeVectorNodeApplicationService implements ManageSolarSailAttitudeVectorNodeUseCase {

    private final SolarSailAttitudeVectorNodeRepositoryPort repositoryPort;

    public SolarSailAttitudeVectorNodeApplicationService(SolarSailAttitudeVectorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SolarSailAttitudeVectorNode createSolarSailAttitudeVectorNode(String tenantId, String title, double value) {
        SolarSailAttitudeVectorNode entity = new SolarSailAttitudeVectorNode(
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
    public Optional<SolarSailAttitudeVectorNode> findSolarSailAttitudeVectorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SolarSailAttitudeVectorNode processOptimization(String id, String tenantId) {
        SolarSailAttitudeVectorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SolarSailAttitudeVectorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
