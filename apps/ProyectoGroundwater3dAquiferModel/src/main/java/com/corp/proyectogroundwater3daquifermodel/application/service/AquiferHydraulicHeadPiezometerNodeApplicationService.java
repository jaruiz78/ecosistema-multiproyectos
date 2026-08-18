package com.corp.proyectogroundwater3daquifermodel.application.service;

import com.corp.proyectogroundwater3daquifermodel.domain.model.AquiferHydraulicHeadPiezometerNode;
import com.corp.proyectogroundwater3daquifermodel.domain.port.in.ManageAquiferHydraulicHeadPiezometerNodeUseCase;
import com.corp.proyectogroundwater3daquifermodel.domain.port.out.AquiferHydraulicHeadPiezometerNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AquiferHydraulicHeadPiezometerNode.
 */
@Service
public class AquiferHydraulicHeadPiezometerNodeApplicationService implements ManageAquiferHydraulicHeadPiezometerNodeUseCase {

    private final AquiferHydraulicHeadPiezometerNodeRepositoryPort repositoryPort;

    public AquiferHydraulicHeadPiezometerNodeApplicationService(AquiferHydraulicHeadPiezometerNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AquiferHydraulicHeadPiezometerNode createAquiferHydraulicHeadPiezometerNode(String tenantId, String title, double value) {
        AquiferHydraulicHeadPiezometerNode entity = new AquiferHydraulicHeadPiezometerNode(
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
    public Optional<AquiferHydraulicHeadPiezometerNode> findAquiferHydraulicHeadPiezometerNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AquiferHydraulicHeadPiezometerNode processOptimization(String id, String tenantId) {
        AquiferHydraulicHeadPiezometerNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AquiferHydraulicHeadPiezometerNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
