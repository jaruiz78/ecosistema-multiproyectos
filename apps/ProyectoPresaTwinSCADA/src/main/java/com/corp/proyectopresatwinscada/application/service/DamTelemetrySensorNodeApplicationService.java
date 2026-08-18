package com.corp.proyectopresatwinscada.application.service;

import com.corp.proyectopresatwinscada.domain.model.DamTelemetrySensorNode;
import com.corp.proyectopresatwinscada.domain.port.in.ManageDamTelemetrySensorNodeUseCase;
import com.corp.proyectopresatwinscada.domain.port.out.DamTelemetrySensorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DamTelemetrySensorNode.
 */
@Service
public class DamTelemetrySensorNodeApplicationService implements ManageDamTelemetrySensorNodeUseCase {

    private final DamTelemetrySensorNodeRepositoryPort repositoryPort;

    public DamTelemetrySensorNodeApplicationService(DamTelemetrySensorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DamTelemetrySensorNode createDamTelemetrySensorNode(String tenantId, String title, double value) {
        DamTelemetrySensorNode entity = new DamTelemetrySensorNode(
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
    public Optional<DamTelemetrySensorNode> findDamTelemetrySensorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DamTelemetrySensorNode processOptimization(String id, String tenantId) {
        DamTelemetrySensorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DamTelemetrySensorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
