package com.corp.proyectosuborbitalspacetourismtwin.application.service;

import com.corp.proyectosuborbitalspacetourismtwin.domain.model.SpaceTourismPassengerGForceNode;
import com.corp.proyectosuborbitalspacetourismtwin.domain.port.in.ManageSpaceTourismPassengerGForceNodeUseCase;
import com.corp.proyectosuborbitalspacetourismtwin.domain.port.out.SpaceTourismPassengerGForceNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpaceTourismPassengerGForceNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpaceTourismPassengerGForceNodeApplicationService implements ManageSpaceTourismPassengerGForceNodeUseCase {

    private final SpaceTourismPassengerGForceNodeRepositoryPort repositoryPort;

    public SpaceTourismPassengerGForceNodeApplicationService(SpaceTourismPassengerGForceNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpaceTourismPassengerGForceNode createSpaceTourismPassengerGForceNode(String tenantId, String title, double value) {
        SpaceTourismPassengerGForceNode entity = new SpaceTourismPassengerGForceNode(
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
    public Optional<SpaceTourismPassengerGForceNode> findSpaceTourismPassengerGForceNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpaceTourismPassengerGForceNode processOptimization(String id, String tenantId) {
        SpaceTourismPassengerGForceNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpaceTourismPassengerGForceNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
