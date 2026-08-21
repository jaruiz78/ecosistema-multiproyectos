package com.corp.proyectopermafrostthawmonitor.application.service;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import com.corp.proyectopermafrostthawmonitor.domain.port.in.ManagePermafrostThawDepthSubsidenceNodeUseCase;
import com.corp.proyectopermafrostthawmonitor.domain.port.out.PermafrostThawDepthSubsidenceNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PermafrostThawDepthSubsidenceNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PermafrostThawDepthSubsidenceNodeApplicationService implements ManagePermafrostThawDepthSubsidenceNodeUseCase {

    private final PermafrostThawDepthSubsidenceNodeRepositoryPort repositoryPort;

    public PermafrostThawDepthSubsidenceNodeApplicationService(PermafrostThawDepthSubsidenceNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PermafrostThawDepthSubsidenceNode createPermafrostThawDepthSubsidenceNode(String tenantId, String title, double value) {
        PermafrostThawDepthSubsidenceNode entity = new PermafrostThawDepthSubsidenceNode(
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
    public Optional<PermafrostThawDepthSubsidenceNode> findPermafrostThawDepthSubsidenceNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PermafrostThawDepthSubsidenceNode processOptimization(String id, String tenantId) {
        PermafrostThawDepthSubsidenceNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PermafrostThawDepthSubsidenceNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
