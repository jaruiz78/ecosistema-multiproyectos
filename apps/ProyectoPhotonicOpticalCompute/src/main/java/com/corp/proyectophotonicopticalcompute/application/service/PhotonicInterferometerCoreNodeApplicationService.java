package com.corp.proyectophotonicopticalcompute.application.service;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import com.corp.proyectophotonicopticalcompute.domain.port.in.ManagePhotonicInterferometerCoreNodeUseCase;
import com.corp.proyectophotonicopticalcompute.domain.port.out.PhotonicInterferometerCoreNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PhotonicInterferometerCoreNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PhotonicInterferometerCoreNodeApplicationService implements ManagePhotonicInterferometerCoreNodeUseCase {

    private final PhotonicInterferometerCoreNodeRepositoryPort repositoryPort;

    public PhotonicInterferometerCoreNodeApplicationService(PhotonicInterferometerCoreNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PhotonicInterferometerCoreNode createPhotonicInterferometerCoreNode(String tenantId, String title, double value) {
        PhotonicInterferometerCoreNode entity = new PhotonicInterferometerCoreNode(
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
    public Optional<PhotonicInterferometerCoreNode> findPhotonicInterferometerCoreNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PhotonicInterferometerCoreNode processOptimization(String id, String tenantId) {
        PhotonicInterferometerCoreNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PhotonicInterferometerCoreNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
