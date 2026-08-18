package com.corp.proyectocoastalsurgeflooddefense.application.service;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import com.corp.proyectocoastalsurgeflooddefense.domain.port.in.ManageStormSurgeElevationForecastNodeUseCase;
import com.corp.proyectocoastalsurgeflooddefense.domain.port.out.StormSurgeElevationForecastNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de StormSurgeElevationForecastNode.
 */
@Service
public class StormSurgeElevationForecastNodeApplicationService implements ManageStormSurgeElevationForecastNodeUseCase {

    private final StormSurgeElevationForecastNodeRepositoryPort repositoryPort;

    public StormSurgeElevationForecastNodeApplicationService(StormSurgeElevationForecastNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public StormSurgeElevationForecastNode createStormSurgeElevationForecastNode(String tenantId, String title, double value) {
        StormSurgeElevationForecastNode entity = new StormSurgeElevationForecastNode(
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
    public Optional<StormSurgeElevationForecastNode> findStormSurgeElevationForecastNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public StormSurgeElevationForecastNode processOptimization(String id, String tenantId) {
        StormSurgeElevationForecastNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        StormSurgeElevationForecastNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
