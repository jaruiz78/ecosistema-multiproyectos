package com.corp.proyectoautonomousoceangliders.application.service;

import com.corp.proyectoautonomousoceangliders.domain.model.AuvSubmarineTrackWaypointNode;
import com.corp.proyectoautonomousoceangliders.domain.port.in.ManageAuvSubmarineTrackWaypointNodeUseCase;
import com.corp.proyectoautonomousoceangliders.domain.port.out.AuvSubmarineTrackWaypointNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AuvSubmarineTrackWaypointNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AuvSubmarineTrackWaypointNodeApplicationService implements ManageAuvSubmarineTrackWaypointNodeUseCase {

    private final AuvSubmarineTrackWaypointNodeRepositoryPort repositoryPort;

    public AuvSubmarineTrackWaypointNodeApplicationService(AuvSubmarineTrackWaypointNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AuvSubmarineTrackWaypointNode createAuvSubmarineTrackWaypointNode(String tenantId, String title, double value) {
        AuvSubmarineTrackWaypointNode entity = new AuvSubmarineTrackWaypointNode(
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
    public Optional<AuvSubmarineTrackWaypointNode> findAuvSubmarineTrackWaypointNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AuvSubmarineTrackWaypointNode processOptimization(String id, String tenantId) {
        AuvSubmarineTrackWaypointNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AuvSubmarineTrackWaypointNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
