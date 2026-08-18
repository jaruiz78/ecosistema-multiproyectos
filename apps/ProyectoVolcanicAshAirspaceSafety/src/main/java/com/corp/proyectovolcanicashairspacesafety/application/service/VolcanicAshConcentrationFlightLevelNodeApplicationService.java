package com.corp.proyectovolcanicashairspacesafety.application.service;

import com.corp.proyectovolcanicashairspacesafety.domain.model.VolcanicAshConcentrationFlightLevelNode;
import com.corp.proyectovolcanicashairspacesafety.domain.port.in.ManageVolcanicAshConcentrationFlightLevelNodeUseCase;
import com.corp.proyectovolcanicashairspacesafety.domain.port.out.VolcanicAshConcentrationFlightLevelNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VolcanicAshConcentrationFlightLevelNode.
 */
@Service
public class VolcanicAshConcentrationFlightLevelNodeApplicationService implements ManageVolcanicAshConcentrationFlightLevelNodeUseCase {

    private final VolcanicAshConcentrationFlightLevelNodeRepositoryPort repositoryPort;

    public VolcanicAshConcentrationFlightLevelNodeApplicationService(VolcanicAshConcentrationFlightLevelNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VolcanicAshConcentrationFlightLevelNode createVolcanicAshConcentrationFlightLevelNode(String tenantId, String title, double value) {
        VolcanicAshConcentrationFlightLevelNode entity = new VolcanicAshConcentrationFlightLevelNode(
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
    public Optional<VolcanicAshConcentrationFlightLevelNode> findVolcanicAshConcentrationFlightLevelNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VolcanicAshConcentrationFlightLevelNode processOptimization(String id, String tenantId) {
        VolcanicAshConcentrationFlightLevelNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VolcanicAshConcentrationFlightLevelNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
