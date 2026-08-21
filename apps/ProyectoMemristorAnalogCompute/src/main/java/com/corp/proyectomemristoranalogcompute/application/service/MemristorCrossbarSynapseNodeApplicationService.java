package com.corp.proyectomemristoranalogcompute.application.service;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import com.corp.proyectomemristoranalogcompute.domain.port.in.ManageMemristorCrossbarSynapseNodeUseCase;
import com.corp.proyectomemristoranalogcompute.domain.port.out.MemristorCrossbarSynapseNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MemristorCrossbarSynapseNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MemristorCrossbarSynapseNodeApplicationService implements ManageMemristorCrossbarSynapseNodeUseCase {

    private final MemristorCrossbarSynapseNodeRepositoryPort repositoryPort;

    public MemristorCrossbarSynapseNodeApplicationService(MemristorCrossbarSynapseNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MemristorCrossbarSynapseNode createMemristorCrossbarSynapseNode(String tenantId, String title, double value) {
        MemristorCrossbarSynapseNode entity = new MemristorCrossbarSynapseNode(
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
    public Optional<MemristorCrossbarSynapseNode> findMemristorCrossbarSynapseNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MemristorCrossbarSynapseNode processOptimization(String id, String tenantId) {
        MemristorCrossbarSynapseNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MemristorCrossbarSynapseNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
