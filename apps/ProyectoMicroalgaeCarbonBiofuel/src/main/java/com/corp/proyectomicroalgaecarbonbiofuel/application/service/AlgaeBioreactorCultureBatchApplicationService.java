package com.corp.proyectomicroalgaecarbonbiofuel.application.service;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.in.ManageAlgaeBioreactorCultureBatchUseCase;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.out.AlgaeBioreactorCultureBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AlgaeBioreactorCultureBatch.
 */
@Service
public class AlgaeBioreactorCultureBatchApplicationService implements ManageAlgaeBioreactorCultureBatchUseCase {

    private final AlgaeBioreactorCultureBatchRepositoryPort repositoryPort;

    public AlgaeBioreactorCultureBatchApplicationService(AlgaeBioreactorCultureBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AlgaeBioreactorCultureBatch createAlgaeBioreactorCultureBatch(String tenantId, String title, double value) {
        AlgaeBioreactorCultureBatch entity = new AlgaeBioreactorCultureBatch(
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
    public Optional<AlgaeBioreactorCultureBatch> findAlgaeBioreactorCultureBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AlgaeBioreactorCultureBatch processOptimization(String id, String tenantId) {
        AlgaeBioreactorCultureBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AlgaeBioreactorCultureBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
