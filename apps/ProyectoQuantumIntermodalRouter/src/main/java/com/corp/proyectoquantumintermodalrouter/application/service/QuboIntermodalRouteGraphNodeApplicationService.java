package com.corp.proyectoquantumintermodalrouter.application.service;

import com.corp.proyectoquantumintermodalrouter.domain.model.QuboIntermodalRouteGraphNode;
import com.corp.proyectoquantumintermodalrouter.domain.port.in.ManageQuboIntermodalRouteGraphNodeUseCase;
import com.corp.proyectoquantumintermodalrouter.domain.port.out.QuboIntermodalRouteGraphNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuboIntermodalRouteGraphNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuboIntermodalRouteGraphNodeApplicationService implements ManageQuboIntermodalRouteGraphNodeUseCase {

    private final QuboIntermodalRouteGraphNodeRepositoryPort repositoryPort;

    public QuboIntermodalRouteGraphNodeApplicationService(QuboIntermodalRouteGraphNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuboIntermodalRouteGraphNode createQuboIntermodalRouteGraphNode(String tenantId, String title, double value) {
        QuboIntermodalRouteGraphNode entity = new QuboIntermodalRouteGraphNode(
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
    public Optional<QuboIntermodalRouteGraphNode> findQuboIntermodalRouteGraphNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuboIntermodalRouteGraphNode processOptimization(String id, String tenantId) {
        QuboIntermodalRouteGraphNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuboIntermodalRouteGraphNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
