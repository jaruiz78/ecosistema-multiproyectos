package com.corp.proyectoquantumdottandemsolar.application.service;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import com.corp.proyectoquantumdottandemsolar.domain.port.in.ManageQuantumDotSolarCellBatchUseCase;
import com.corp.proyectoquantumdottandemsolar.domain.port.out.QuantumDotSolarCellBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuantumDotSolarCellBatch.
 */
@Service
public class QuantumDotSolarCellBatchApplicationService implements ManageQuantumDotSolarCellBatchUseCase {

    private final QuantumDotSolarCellBatchRepositoryPort repositoryPort;

    public QuantumDotSolarCellBatchApplicationService(QuantumDotSolarCellBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumDotSolarCellBatch createQuantumDotSolarCellBatch(String tenantId, String title, double value) {
        QuantumDotSolarCellBatch entity = new QuantumDotSolarCellBatch(
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
    public Optional<QuantumDotSolarCellBatch> findQuantumDotSolarCellBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumDotSolarCellBatch processOptimization(String id, String tenantId) {
        QuantumDotSolarCellBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumDotSolarCellBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
