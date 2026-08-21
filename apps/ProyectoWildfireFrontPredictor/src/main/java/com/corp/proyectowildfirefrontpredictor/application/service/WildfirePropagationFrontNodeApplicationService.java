package com.corp.proyectowildfirefrontpredictor.application.service;

import com.corp.proyectowildfirefrontpredictor.domain.model.WildfirePropagationFrontNode;
import com.corp.proyectowildfirefrontpredictor.domain.port.in.ManageWildfirePropagationFrontNodeUseCase;
import com.corp.proyectowildfirefrontpredictor.domain.port.out.WildfirePropagationFrontNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de WildfirePropagationFrontNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class WildfirePropagationFrontNodeApplicationService implements ManageWildfirePropagationFrontNodeUseCase {

    private final WildfirePropagationFrontNodeRepositoryPort repositoryPort;

    public WildfirePropagationFrontNodeApplicationService(WildfirePropagationFrontNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public WildfirePropagationFrontNode createWildfirePropagationFrontNode(String tenantId, String title, double value) {
        WildfirePropagationFrontNode entity = new WildfirePropagationFrontNode(
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
    public Optional<WildfirePropagationFrontNode> findWildfirePropagationFrontNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public WildfirePropagationFrontNode processOptimization(String id, String tenantId) {
        WildfirePropagationFrontNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        WildfirePropagationFrontNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
