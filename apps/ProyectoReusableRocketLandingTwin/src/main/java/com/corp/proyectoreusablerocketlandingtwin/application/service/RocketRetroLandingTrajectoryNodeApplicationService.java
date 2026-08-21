package com.corp.proyectoreusablerocketlandingtwin.application.service;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import com.corp.proyectoreusablerocketlandingtwin.domain.port.in.ManageRocketRetroLandingTrajectoryNodeUseCase;
import com.corp.proyectoreusablerocketlandingtwin.domain.port.out.RocketRetroLandingTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de RocketRetroLandingTrajectoryNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class RocketRetroLandingTrajectoryNodeApplicationService implements ManageRocketRetroLandingTrajectoryNodeUseCase {

    private final RocketRetroLandingTrajectoryNodeRepositoryPort repositoryPort;

    public RocketRetroLandingTrajectoryNodeApplicationService(RocketRetroLandingTrajectoryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RocketRetroLandingTrajectoryNode createRocketRetroLandingTrajectoryNode(String tenantId, String title, double value) {
        RocketRetroLandingTrajectoryNode entity = new RocketRetroLandingTrajectoryNode(
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
    public Optional<RocketRetroLandingTrajectoryNode> findRocketRetroLandingTrajectoryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RocketRetroLandingTrajectoryNode processOptimization(String id, String tenantId) {
        RocketRetroLandingTrajectoryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RocketRetroLandingTrajectoryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
