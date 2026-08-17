package com.corp.proyectohidrogeno.application.service;

import com.corp.proyectohidrogeno.domain.model.ElectrolyzerUnit;
import com.corp.proyectohidrogeno.domain.port.in.ManageElectrolyzerUnitUseCase;
import com.corp.proyectohidrogeno.domain.port.out.ElectrolyzerUnitRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ElectrolyzerUnit.
 */
@Service
public class ElectrolyzerUnitApplicationService implements ManageElectrolyzerUnitUseCase {

    private final ElectrolyzerUnitRepositoryPort repositoryPort;

    public ElectrolyzerUnitApplicationService(ElectrolyzerUnitRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ElectrolyzerUnit createElectrolyzerUnit(String tenantId, String title, double value) {
        ElectrolyzerUnit entity = new ElectrolyzerUnit(
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
    public Optional<ElectrolyzerUnit> findElectrolyzerUnitById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ElectrolyzerUnit processOptimization(String id, String tenantId) {
        ElectrolyzerUnit existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ElectrolyzerUnit optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
