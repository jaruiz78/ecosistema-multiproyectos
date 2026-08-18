package com.corp.proyectoepigeneticbioagemonitor.application.service;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.in.ManageCpgMethylationProfileNodeUseCase;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.out.CpgMethylationProfileNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CpgMethylationProfileNode.
 */
@Service
public class CpgMethylationProfileNodeApplicationService implements ManageCpgMethylationProfileNodeUseCase {

    private final CpgMethylationProfileNodeRepositoryPort repositoryPort;

    public CpgMethylationProfileNodeApplicationService(CpgMethylationProfileNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CpgMethylationProfileNode createCpgMethylationProfileNode(String tenantId, String title, double value) {
        CpgMethylationProfileNode entity = new CpgMethylationProfileNode(
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
    public Optional<CpgMethylationProfileNode> findCpgMethylationProfileNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CpgMethylationProfileNode processOptimization(String id, String tenantId) {
        CpgMethylationProfileNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CpgMethylationProfileNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
