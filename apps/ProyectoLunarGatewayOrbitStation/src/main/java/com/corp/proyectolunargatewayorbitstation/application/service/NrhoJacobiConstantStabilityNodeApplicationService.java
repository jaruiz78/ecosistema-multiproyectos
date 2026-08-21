package com.corp.proyectolunargatewayorbitstation.application.service;

import com.corp.proyectolunargatewayorbitstation.domain.model.NrhoJacobiConstantStabilityNode;
import com.corp.proyectolunargatewayorbitstation.domain.port.in.ManageNrhoJacobiConstantStabilityNodeUseCase;
import com.corp.proyectolunargatewayorbitstation.domain.port.out.NrhoJacobiConstantStabilityNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de NrhoJacobiConstantStabilityNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class NrhoJacobiConstantStabilityNodeApplicationService implements ManageNrhoJacobiConstantStabilityNodeUseCase {

    private final NrhoJacobiConstantStabilityNodeRepositoryPort repositoryPort;

    public NrhoJacobiConstantStabilityNodeApplicationService(NrhoJacobiConstantStabilityNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public NrhoJacobiConstantStabilityNode createNrhoJacobiConstantStabilityNode(String tenantId, String title, double value) {
        NrhoJacobiConstantStabilityNode entity = new NrhoJacobiConstantStabilityNode(
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
    public Optional<NrhoJacobiConstantStabilityNode> findNrhoJacobiConstantStabilityNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public NrhoJacobiConstantStabilityNode processOptimization(String id, String tenantId) {
        NrhoJacobiConstantStabilityNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        NrhoJacobiConstantStabilityNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
