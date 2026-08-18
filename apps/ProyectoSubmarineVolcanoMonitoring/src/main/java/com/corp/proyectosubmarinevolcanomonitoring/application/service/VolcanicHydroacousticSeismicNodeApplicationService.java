package com.corp.proyectosubmarinevolcanomonitoring.application.service;

import com.corp.proyectosubmarinevolcanomonitoring.domain.model.VolcanicHydroacousticSeismicNode;
import com.corp.proyectosubmarinevolcanomonitoring.domain.port.in.ManageVolcanicHydroacousticSeismicNodeUseCase;
import com.corp.proyectosubmarinevolcanomonitoring.domain.port.out.VolcanicHydroacousticSeismicNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VolcanicHydroacousticSeismicNode.
 */
@Service
public class VolcanicHydroacousticSeismicNodeApplicationService implements ManageVolcanicHydroacousticSeismicNodeUseCase {

    private final VolcanicHydroacousticSeismicNodeRepositoryPort repositoryPort;

    public VolcanicHydroacousticSeismicNodeApplicationService(VolcanicHydroacousticSeismicNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VolcanicHydroacousticSeismicNode createVolcanicHydroacousticSeismicNode(String tenantId, String title, double value) {
        VolcanicHydroacousticSeismicNode entity = new VolcanicHydroacousticSeismicNode(
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
    public Optional<VolcanicHydroacousticSeismicNode> findVolcanicHydroacousticSeismicNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VolcanicHydroacousticSeismicNode processOptimization(String id, String tenantId) {
        VolcanicHydroacousticSeismicNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VolcanicHydroacousticSeismicNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
