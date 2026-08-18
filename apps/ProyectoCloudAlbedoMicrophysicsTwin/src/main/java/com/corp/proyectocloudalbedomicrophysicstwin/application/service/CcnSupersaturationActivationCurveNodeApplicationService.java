package com.corp.proyectocloudalbedomicrophysicstwin.application.service;

import com.corp.proyectocloudalbedomicrophysicstwin.domain.model.CcnSupersaturationActivationCurveNode;
import com.corp.proyectocloudalbedomicrophysicstwin.domain.port.in.ManageCcnSupersaturationActivationCurveNodeUseCase;
import com.corp.proyectocloudalbedomicrophysicstwin.domain.port.out.CcnSupersaturationActivationCurveNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CcnSupersaturationActivationCurveNode.
 */
@Service
public class CcnSupersaturationActivationCurveNodeApplicationService implements ManageCcnSupersaturationActivationCurveNodeUseCase {

    private final CcnSupersaturationActivationCurveNodeRepositoryPort repositoryPort;

    public CcnSupersaturationActivationCurveNodeApplicationService(CcnSupersaturationActivationCurveNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CcnSupersaturationActivationCurveNode createCcnSupersaturationActivationCurveNode(String tenantId, String title, double value) {
        CcnSupersaturationActivationCurveNode entity = new CcnSupersaturationActivationCurveNode(
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
    public Optional<CcnSupersaturationActivationCurveNode> findCcnSupersaturationActivationCurveNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CcnSupersaturationActivationCurveNode processOptimization(String id, String tenantId) {
        CcnSupersaturationActivationCurveNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CcnSupersaturationActivationCurveNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
