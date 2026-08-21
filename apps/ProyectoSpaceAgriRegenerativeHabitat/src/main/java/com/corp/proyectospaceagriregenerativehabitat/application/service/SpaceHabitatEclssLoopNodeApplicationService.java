package com.corp.proyectospaceagriregenerativehabitat.application.service;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import com.corp.proyectospaceagriregenerativehabitat.domain.port.in.ManageSpaceHabitatEclssLoopNodeUseCase;
import com.corp.proyectospaceagriregenerativehabitat.domain.port.out.SpaceHabitatEclssLoopNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpaceHabitatEclssLoopNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpaceHabitatEclssLoopNodeApplicationService implements ManageSpaceHabitatEclssLoopNodeUseCase {

    private final SpaceHabitatEclssLoopNodeRepositoryPort repositoryPort;

    public SpaceHabitatEclssLoopNodeApplicationService(SpaceHabitatEclssLoopNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpaceHabitatEclssLoopNode createSpaceHabitatEclssLoopNode(String tenantId, String title, double value) {
        SpaceHabitatEclssLoopNode entity = new SpaceHabitatEclssLoopNode(
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
    public Optional<SpaceHabitatEclssLoopNode> findSpaceHabitatEclssLoopNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpaceHabitatEclssLoopNode processOptimization(String id, String tenantId) {
        SpaceHabitatEclssLoopNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpaceHabitatEclssLoopNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
