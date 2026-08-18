package com.corp.proyectomarinebioacousticssonar.application.service;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import com.corp.proyectomarinebioacousticssonar.domain.port.in.ManageHydrophoneAcousticPulseNodeUseCase;
import com.corp.proyectomarinebioacousticssonar.domain.port.out.HydrophoneAcousticPulseNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HydrophoneAcousticPulseNode.
 */
@Service
public class HydrophoneAcousticPulseNodeApplicationService implements ManageHydrophoneAcousticPulseNodeUseCase {

    private final HydrophoneAcousticPulseNodeRepositoryPort repositoryPort;

    public HydrophoneAcousticPulseNodeApplicationService(HydrophoneAcousticPulseNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HydrophoneAcousticPulseNode createHydrophoneAcousticPulseNode(String tenantId, String title, double value) {
        HydrophoneAcousticPulseNode entity = new HydrophoneAcousticPulseNode(
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
    public Optional<HydrophoneAcousticPulseNode> findHydrophoneAcousticPulseNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HydrophoneAcousticPulseNode processOptimization(String id, String tenantId) {
        HydrophoneAcousticPulseNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HydrophoneAcousticPulseNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
