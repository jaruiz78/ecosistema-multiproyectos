package com.corp.proyectophotoniccrystalwaveguide.application.service;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import com.corp.proyectophotoniccrystalwaveguide.domain.port.in.ManagePhotonicWaveguideCouplerNodeUseCase;
import com.corp.proyectophotoniccrystalwaveguide.domain.port.out.PhotonicWaveguideCouplerNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PhotonicWaveguideCouplerNode.
 */
@Service
public class PhotonicWaveguideCouplerNodeApplicationService implements ManagePhotonicWaveguideCouplerNodeUseCase {

    private final PhotonicWaveguideCouplerNodeRepositoryPort repositoryPort;

    public PhotonicWaveguideCouplerNodeApplicationService(PhotonicWaveguideCouplerNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PhotonicWaveguideCouplerNode createPhotonicWaveguideCouplerNode(String tenantId, String title, double value) {
        PhotonicWaveguideCouplerNode entity = new PhotonicWaveguideCouplerNode(
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
    public Optional<PhotonicWaveguideCouplerNode> findPhotonicWaveguideCouplerNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PhotonicWaveguideCouplerNode processOptimization(String id, String tenantId) {
        PhotonicWaveguideCouplerNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PhotonicWaveguideCouplerNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
