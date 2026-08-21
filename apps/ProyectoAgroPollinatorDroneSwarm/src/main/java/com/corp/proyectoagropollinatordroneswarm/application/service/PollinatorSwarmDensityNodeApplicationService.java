package com.corp.proyectoagropollinatordroneswarm.application.service;

import com.corp.proyectoagropollinatordroneswarm.domain.model.PollinatorSwarmDensityNode;
import com.corp.proyectoagropollinatordroneswarm.domain.port.in.ManagePollinatorSwarmDensityNodeUseCase;
import com.corp.proyectoagropollinatordroneswarm.domain.port.out.PollinatorSwarmDensityNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PollinatorSwarmDensityNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PollinatorSwarmDensityNodeApplicationService implements ManagePollinatorSwarmDensityNodeUseCase {

    private final PollinatorSwarmDensityNodeRepositoryPort repositoryPort;

    public PollinatorSwarmDensityNodeApplicationService(PollinatorSwarmDensityNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PollinatorSwarmDensityNode createPollinatorSwarmDensityNode(String tenantId, String title, double value) {
        PollinatorSwarmDensityNode entity = new PollinatorSwarmDensityNode(
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
    public Optional<PollinatorSwarmDensityNode> findPollinatorSwarmDensityNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PollinatorSwarmDensityNode processOptimization(String id, String tenantId) {
        PollinatorSwarmDensityNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PollinatorSwarmDensityNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
