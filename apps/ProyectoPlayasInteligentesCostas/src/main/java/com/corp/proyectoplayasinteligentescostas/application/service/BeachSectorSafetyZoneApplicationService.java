package com.corp.proyectoplayasinteligentescostas.application.service;

import com.corp.proyectoplayasinteligentescostas.domain.model.BeachSectorSafetyZone;
import com.corp.proyectoplayasinteligentescostas.domain.port.in.ManageBeachSectorSafetyZoneUseCase;
import com.corp.proyectoplayasinteligentescostas.domain.port.out.BeachSectorSafetyZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BeachSectorSafetyZone.
 */
@Service
public class BeachSectorSafetyZoneApplicationService implements ManageBeachSectorSafetyZoneUseCase {

    private final BeachSectorSafetyZoneRepositoryPort repositoryPort;

    public BeachSectorSafetyZoneApplicationService(BeachSectorSafetyZoneRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BeachSectorSafetyZone createBeachSectorSafetyZone(String tenantId, String title, double value) {
        BeachSectorSafetyZone entity = new BeachSectorSafetyZone(
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
    public Optional<BeachSectorSafetyZone> findBeachSectorSafetyZoneById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BeachSectorSafetyZone processOptimization(String id, String tenantId) {
        BeachSectorSafetyZone existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BeachSectorSafetyZone optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
