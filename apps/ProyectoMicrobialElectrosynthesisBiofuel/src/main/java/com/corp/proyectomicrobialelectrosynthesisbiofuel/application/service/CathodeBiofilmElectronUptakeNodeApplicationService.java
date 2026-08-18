package com.corp.proyectomicrobialelectrosynthesisbiofuel.application.service;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.in.ManageCathodeBiofilmElectronUptakeNodeUseCase;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.out.CathodeBiofilmElectronUptakeNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CathodeBiofilmElectronUptakeNode.
 */
@Service
public class CathodeBiofilmElectronUptakeNodeApplicationService implements ManageCathodeBiofilmElectronUptakeNodeUseCase {

    private final CathodeBiofilmElectronUptakeNodeRepositoryPort repositoryPort;

    public CathodeBiofilmElectronUptakeNodeApplicationService(CathodeBiofilmElectronUptakeNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CathodeBiofilmElectronUptakeNode createCathodeBiofilmElectronUptakeNode(String tenantId, String title, double value) {
        CathodeBiofilmElectronUptakeNode entity = new CathodeBiofilmElectronUptakeNode(
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
    public Optional<CathodeBiofilmElectronUptakeNode> findCathodeBiofilmElectronUptakeNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CathodeBiofilmElectronUptakeNode processOptimization(String id, String tenantId) {
        CathodeBiofilmElectronUptakeNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CathodeBiofilmElectronUptakeNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
