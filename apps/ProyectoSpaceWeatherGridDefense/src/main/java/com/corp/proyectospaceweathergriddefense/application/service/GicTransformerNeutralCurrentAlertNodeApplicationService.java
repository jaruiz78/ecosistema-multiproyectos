package com.corp.proyectospaceweathergriddefense.application.service;

import com.corp.proyectospaceweathergriddefense.domain.model.GicTransformerNeutralCurrentAlertNode;
import com.corp.proyectospaceweathergriddefense.domain.port.in.ManageGicTransformerNeutralCurrentAlertNodeUseCase;
import com.corp.proyectospaceweathergriddefense.domain.port.out.GicTransformerNeutralCurrentAlertNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GicTransformerNeutralCurrentAlertNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GicTransformerNeutralCurrentAlertNodeApplicationService implements ManageGicTransformerNeutralCurrentAlertNodeUseCase {

    private final GicTransformerNeutralCurrentAlertNodeRepositoryPort repositoryPort;

    public GicTransformerNeutralCurrentAlertNodeApplicationService(GicTransformerNeutralCurrentAlertNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GicTransformerNeutralCurrentAlertNode createGicTransformerNeutralCurrentAlertNode(String tenantId, String title, double value) {
        GicTransformerNeutralCurrentAlertNode entity = new GicTransformerNeutralCurrentAlertNode(
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
    public Optional<GicTransformerNeutralCurrentAlertNode> findGicTransformerNeutralCurrentAlertNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GicTransformerNeutralCurrentAlertNode processOptimization(String id, String tenantId) {
        GicTransformerNeutralCurrentAlertNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GicTransformerNeutralCurrentAlertNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
