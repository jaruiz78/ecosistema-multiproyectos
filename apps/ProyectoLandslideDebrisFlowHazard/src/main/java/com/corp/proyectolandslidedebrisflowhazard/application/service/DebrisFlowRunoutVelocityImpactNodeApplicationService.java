package com.corp.proyectolandslidedebrisflowhazard.application.service;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import com.corp.proyectolandslidedebrisflowhazard.domain.port.in.ManageDebrisFlowRunoutVelocityImpactNodeUseCase;
import com.corp.proyectolandslidedebrisflowhazard.domain.port.out.DebrisFlowRunoutVelocityImpactNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DebrisFlowRunoutVelocityImpactNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DebrisFlowRunoutVelocityImpactNodeApplicationService implements ManageDebrisFlowRunoutVelocityImpactNodeUseCase {

    private final DebrisFlowRunoutVelocityImpactNodeRepositoryPort repositoryPort;

    public DebrisFlowRunoutVelocityImpactNodeApplicationService(DebrisFlowRunoutVelocityImpactNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DebrisFlowRunoutVelocityImpactNode createDebrisFlowRunoutVelocityImpactNode(String tenantId, String title, double value) {
        DebrisFlowRunoutVelocityImpactNode entity = new DebrisFlowRunoutVelocityImpactNode(
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
    public Optional<DebrisFlowRunoutVelocityImpactNode> findDebrisFlowRunoutVelocityImpactNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DebrisFlowRunoutVelocityImpactNode processOptimization(String id, String tenantId) {
        DebrisFlowRunoutVelocityImpactNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DebrisFlowRunoutVelocityImpactNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
