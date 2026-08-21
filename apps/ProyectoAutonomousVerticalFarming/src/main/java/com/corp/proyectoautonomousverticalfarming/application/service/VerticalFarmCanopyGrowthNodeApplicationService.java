package com.corp.proyectoautonomousverticalfarming.application.service;

import com.corp.proyectoautonomousverticalfarming.domain.model.VerticalFarmCanopyGrowthNode;
import com.corp.proyectoautonomousverticalfarming.domain.port.in.ManageVerticalFarmCanopyGrowthNodeUseCase;
import com.corp.proyectoautonomousverticalfarming.domain.port.out.VerticalFarmCanopyGrowthNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VerticalFarmCanopyGrowthNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class VerticalFarmCanopyGrowthNodeApplicationService implements ManageVerticalFarmCanopyGrowthNodeUseCase {

    private final VerticalFarmCanopyGrowthNodeRepositoryPort repositoryPort;

    public VerticalFarmCanopyGrowthNodeApplicationService(VerticalFarmCanopyGrowthNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VerticalFarmCanopyGrowthNode createVerticalFarmCanopyGrowthNode(String tenantId, String title, double value) {
        VerticalFarmCanopyGrowthNode entity = new VerticalFarmCanopyGrowthNode(
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
    public Optional<VerticalFarmCanopyGrowthNode> findVerticalFarmCanopyGrowthNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VerticalFarmCanopyGrowthNode processOptimization(String id, String tenantId) {
        VerticalFarmCanopyGrowthNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VerticalFarmCanopyGrowthNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
