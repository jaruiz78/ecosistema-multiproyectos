package com.corp.proyectolitertedgeinferencehub.application.service;

import com.corp.proyectolitertedgeinferencehub.domain.model.LiteRtQuantizedModelExecutionNode;
import com.corp.proyectolitertedgeinferencehub.domain.port.in.ManageLiteRtQuantizedModelExecutionNodeUseCase;
import com.corp.proyectolitertedgeinferencehub.domain.port.out.LiteRtQuantizedModelExecutionNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LiteRtQuantizedModelExecutionNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LiteRtQuantizedModelExecutionNodeApplicationService implements ManageLiteRtQuantizedModelExecutionNodeUseCase {

    private final LiteRtQuantizedModelExecutionNodeRepositoryPort repositoryPort;

    public LiteRtQuantizedModelExecutionNodeApplicationService(LiteRtQuantizedModelExecutionNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LiteRtQuantizedModelExecutionNode createLiteRtQuantizedModelExecutionNode(String tenantId, String title, double value) {
        LiteRtQuantizedModelExecutionNode entity = new LiteRtQuantizedModelExecutionNode(
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
    public Optional<LiteRtQuantizedModelExecutionNode> findLiteRtQuantizedModelExecutionNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LiteRtQuantizedModelExecutionNode processOptimization(String id, String tenantId) {
        LiteRtQuantizedModelExecutionNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LiteRtQuantizedModelExecutionNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
