package com.corp.proyectodistrictheatingcoolingtwin.application.service;

import com.corp.proyectodistrictheatingcoolingtwin.domain.model.DistrictThermalSubstationNode;
import com.corp.proyectodistrictheatingcoolingtwin.domain.port.in.ManageDistrictThermalSubstationNodeUseCase;
import com.corp.proyectodistrictheatingcoolingtwin.domain.port.out.DistrictThermalSubstationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DistrictThermalSubstationNode.
 */
@Service
public class DistrictThermalSubstationNodeApplicationService implements ManageDistrictThermalSubstationNodeUseCase {

    private final DistrictThermalSubstationNodeRepositoryPort repositoryPort;

    public DistrictThermalSubstationNodeApplicationService(DistrictThermalSubstationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DistrictThermalSubstationNode createDistrictThermalSubstationNode(String tenantId, String title, double value) {
        DistrictThermalSubstationNode entity = new DistrictThermalSubstationNode(
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
    public Optional<DistrictThermalSubstationNode> findDistrictThermalSubstationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DistrictThermalSubstationNode processOptimization(String id, String tenantId) {
        DistrictThermalSubstationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DistrictThermalSubstationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
