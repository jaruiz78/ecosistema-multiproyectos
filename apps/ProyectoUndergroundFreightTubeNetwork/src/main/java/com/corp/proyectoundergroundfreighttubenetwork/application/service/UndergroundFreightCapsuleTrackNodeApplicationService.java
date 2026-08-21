package com.corp.proyectoundergroundfreighttubenetwork.application.service;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.in.ManageUndergroundFreightCapsuleTrackNodeUseCase;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.out.UndergroundFreightCapsuleTrackNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de UndergroundFreightCapsuleTrackNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class UndergroundFreightCapsuleTrackNodeApplicationService implements ManageUndergroundFreightCapsuleTrackNodeUseCase {

    private final UndergroundFreightCapsuleTrackNodeRepositoryPort repositoryPort;

    public UndergroundFreightCapsuleTrackNodeApplicationService(UndergroundFreightCapsuleTrackNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UndergroundFreightCapsuleTrackNode createUndergroundFreightCapsuleTrackNode(String tenantId, String title, double value) {
        UndergroundFreightCapsuleTrackNode entity = new UndergroundFreightCapsuleTrackNode(
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
    public Optional<UndergroundFreightCapsuleTrackNode> findUndergroundFreightCapsuleTrackNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public UndergroundFreightCapsuleTrackNode processOptimization(String id, String tenantId) {
        UndergroundFreightCapsuleTrackNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        UndergroundFreightCapsuleTrackNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
