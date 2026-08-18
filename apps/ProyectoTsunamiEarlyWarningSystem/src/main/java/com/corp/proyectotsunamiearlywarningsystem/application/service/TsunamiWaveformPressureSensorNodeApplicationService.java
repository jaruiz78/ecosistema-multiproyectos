package com.corp.proyectotsunamiearlywarningsystem.application.service;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.in.ManageTsunamiWaveformPressureSensorNodeUseCase;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.out.TsunamiWaveformPressureSensorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TsunamiWaveformPressureSensorNode.
 */
@Service
public class TsunamiWaveformPressureSensorNodeApplicationService implements ManageTsunamiWaveformPressureSensorNodeUseCase {

    private final TsunamiWaveformPressureSensorNodeRepositoryPort repositoryPort;

    public TsunamiWaveformPressureSensorNodeApplicationService(TsunamiWaveformPressureSensorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TsunamiWaveformPressureSensorNode createTsunamiWaveformPressureSensorNode(String tenantId, String title, double value) {
        TsunamiWaveformPressureSensorNode entity = new TsunamiWaveformPressureSensorNode(
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
    public Optional<TsunamiWaveformPressureSensorNode> findTsunamiWaveformPressureSensorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TsunamiWaveformPressureSensorNode processOptimization(String id, String tenantId) {
        TsunamiWaveformPressureSensorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TsunamiWaveformPressureSensorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
