package com.corp.proyectoquantumsatellitesar.application.service;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import com.corp.proyectoquantumsatellitesar.domain.port.in.ManageInSarDisplacementTrackNodeUseCase;
import com.corp.proyectoquantumsatellitesar.domain.port.out.InSarDisplacementTrackNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de InSarDisplacementTrackNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InSarDisplacementTrackNodeApplicationService implements ManageInSarDisplacementTrackNodeUseCase {

    private final InSarDisplacementTrackNodeRepositoryPort repositoryPort;

    public InSarDisplacementTrackNodeApplicationService(InSarDisplacementTrackNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public InSarDisplacementTrackNode createInSarDisplacementTrackNode(String tenantId, String title, double value) {
        InSarDisplacementTrackNode entity = new InSarDisplacementTrackNode(
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
    public Optional<InSarDisplacementTrackNode> findInSarDisplacementTrackNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public InSarDisplacementTrackNode processOptimization(String id, String tenantId) {
        InSarDisplacementTrackNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        InSarDisplacementTrackNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
