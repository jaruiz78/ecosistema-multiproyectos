package com.corp.proyectoquantummetrologycalibration.application.service;

import com.corp.proyectoquantummetrologycalibration.domain.model.QuantumHallPlateauResistanceToken;
import com.corp.proyectoquantummetrologycalibration.domain.port.in.ManageQuantumHallPlateauResistanceTokenUseCase;
import com.corp.proyectoquantummetrologycalibration.domain.port.out.QuantumHallPlateauResistanceTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuantumHallPlateauResistanceToken.
 */
@Service
public class QuantumHallPlateauResistanceTokenApplicationService implements ManageQuantumHallPlateauResistanceTokenUseCase {

    private final QuantumHallPlateauResistanceTokenRepositoryPort repositoryPort;

    public QuantumHallPlateauResistanceTokenApplicationService(QuantumHallPlateauResistanceTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumHallPlateauResistanceToken createQuantumHallPlateauResistanceToken(String tenantId, String title, double value) {
        QuantumHallPlateauResistanceToken entity = new QuantumHallPlateauResistanceToken(
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
    public Optional<QuantumHallPlateauResistanceToken> findQuantumHallPlateauResistanceTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumHallPlateauResistanceToken processOptimization(String id, String tenantId) {
        QuantumHallPlateauResistanceToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumHallPlateauResistanceToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
