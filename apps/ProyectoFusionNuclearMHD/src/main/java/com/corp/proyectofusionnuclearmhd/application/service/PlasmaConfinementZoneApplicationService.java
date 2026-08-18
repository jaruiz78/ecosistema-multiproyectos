package com.corp.proyectofusionnuclearmhd.application.service;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import com.corp.proyectofusionnuclearmhd.domain.port.in.ManagePlasmaConfinementZoneUseCase;
import com.corp.proyectofusionnuclearmhd.domain.port.out.PlasmaConfinementZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlasmaConfinementZone.
 */
@Service
public class PlasmaConfinementZoneApplicationService implements ManagePlasmaConfinementZoneUseCase {

    private final PlasmaConfinementZoneRepositoryPort repositoryPort;

    public PlasmaConfinementZoneApplicationService(PlasmaConfinementZoneRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlasmaConfinementZone createPlasmaConfinementZone(String tenantId, String title, double value) {
        PlasmaConfinementZone entity = new PlasmaConfinementZone(
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
    public Optional<PlasmaConfinementZone> findPlasmaConfinementZoneById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlasmaConfinementZone processOptimization(String id, String tenantId) {
        PlasmaConfinementZone existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlasmaConfinementZone optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
