package com.corp.proyectoprecisionsoilregen.application.service;

import com.corp.proyectoprecisionsoilregen.domain.model.SoilCarbonMeasurement;
import com.corp.proyectoprecisionsoilregen.domain.port.in.ManageSoilCarbonMeasurementUseCase;
import com.corp.proyectoprecisionsoilregen.domain.port.out.SoilCarbonMeasurementRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SoilCarbonMeasurement.
 */
@Service
public class SoilCarbonMeasurementApplicationService implements ManageSoilCarbonMeasurementUseCase {

    private final SoilCarbonMeasurementRepositoryPort repositoryPort;

    public SoilCarbonMeasurementApplicationService(SoilCarbonMeasurementRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SoilCarbonMeasurement createSoilCarbonMeasurement(String tenantId, String title, double value) {
        SoilCarbonMeasurement entity = new SoilCarbonMeasurement(
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
    public Optional<SoilCarbonMeasurement> findSoilCarbonMeasurementById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SoilCarbonMeasurement processOptimization(String id, String tenantId) {
        SoilCarbonMeasurement existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SoilCarbonMeasurement optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
