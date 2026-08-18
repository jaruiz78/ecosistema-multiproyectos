package com.corp.proyectocoastalclifferosiondefense.application.service;

import com.corp.proyectocoastalclifferosiondefense.domain.model.CliffRetreatErosionRateNode;
import com.corp.proyectocoastalclifferosiondefense.domain.port.in.ManageCliffRetreatErosionRateNodeUseCase;
import com.corp.proyectocoastalclifferosiondefense.domain.port.out.CliffRetreatErosionRateNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CliffRetreatErosionRateNode.
 */
@Service
public class CliffRetreatErosionRateNodeApplicationService implements ManageCliffRetreatErosionRateNodeUseCase {

    private final CliffRetreatErosionRateNodeRepositoryPort repositoryPort;

    public CliffRetreatErosionRateNodeApplicationService(CliffRetreatErosionRateNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CliffRetreatErosionRateNode createCliffRetreatErosionRateNode(String tenantId, String title, double value) {
        CliffRetreatErosionRateNode entity = new CliffRetreatErosionRateNode(
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
    public Optional<CliffRetreatErosionRateNode> findCliffRetreatErosionRateNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CliffRetreatErosionRateNode processOptimization(String id, String tenantId) {
        CliffRetreatErosionRateNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CliffRetreatErosionRateNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
