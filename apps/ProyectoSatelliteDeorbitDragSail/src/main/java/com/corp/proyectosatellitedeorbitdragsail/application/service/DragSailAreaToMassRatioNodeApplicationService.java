package com.corp.proyectosatellitedeorbitdragsail.application.service;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import com.corp.proyectosatellitedeorbitdragsail.domain.port.in.ManageDragSailAreaToMassRatioNodeUseCase;
import com.corp.proyectosatellitedeorbitdragsail.domain.port.out.DragSailAreaToMassRatioNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DragSailAreaToMassRatioNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DragSailAreaToMassRatioNodeApplicationService implements ManageDragSailAreaToMassRatioNodeUseCase {

    private final DragSailAreaToMassRatioNodeRepositoryPort repositoryPort;

    public DragSailAreaToMassRatioNodeApplicationService(DragSailAreaToMassRatioNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DragSailAreaToMassRatioNode createDragSailAreaToMassRatioNode(String tenantId, String title, double value) {
        DragSailAreaToMassRatioNode entity = new DragSailAreaToMassRatioNode(
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
    public Optional<DragSailAreaToMassRatioNode> findDragSailAreaToMassRatioNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DragSailAreaToMassRatioNode processOptimization(String id, String tenantId) {
        DragSailAreaToMassRatioNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DragSailAreaToMassRatioNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
