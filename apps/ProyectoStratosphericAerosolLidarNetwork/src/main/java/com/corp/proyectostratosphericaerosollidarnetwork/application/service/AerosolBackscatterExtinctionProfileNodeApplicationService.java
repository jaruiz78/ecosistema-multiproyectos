package com.corp.proyectostratosphericaerosollidarnetwork.application.service;

import com.corp.proyectostratosphericaerosollidarnetwork.domain.model.AerosolBackscatterExtinctionProfileNode;
import com.corp.proyectostratosphericaerosollidarnetwork.domain.port.in.ManageAerosolBackscatterExtinctionProfileNodeUseCase;
import com.corp.proyectostratosphericaerosollidarnetwork.domain.port.out.AerosolBackscatterExtinctionProfileNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AerosolBackscatterExtinctionProfileNode.
 */
@Service
public class AerosolBackscatterExtinctionProfileNodeApplicationService implements ManageAerosolBackscatterExtinctionProfileNodeUseCase {

    private final AerosolBackscatterExtinctionProfileNodeRepositoryPort repositoryPort;

    public AerosolBackscatterExtinctionProfileNodeApplicationService(AerosolBackscatterExtinctionProfileNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AerosolBackscatterExtinctionProfileNode createAerosolBackscatterExtinctionProfileNode(String tenantId, String title, double value) {
        AerosolBackscatterExtinctionProfileNode entity = new AerosolBackscatterExtinctionProfileNode(
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
    public Optional<AerosolBackscatterExtinctionProfileNode> findAerosolBackscatterExtinctionProfileNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AerosolBackscatterExtinctionProfileNode processOptimization(String id, String tenantId) {
        AerosolBackscatterExtinctionProfileNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AerosolBackscatterExtinctionProfileNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
