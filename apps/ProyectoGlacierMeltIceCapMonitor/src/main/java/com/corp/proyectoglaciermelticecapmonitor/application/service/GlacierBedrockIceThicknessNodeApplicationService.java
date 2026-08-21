package com.corp.proyectoglaciermelticecapmonitor.application.service;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import com.corp.proyectoglaciermelticecapmonitor.domain.port.in.ManageGlacierBedrockIceThicknessNodeUseCase;
import com.corp.proyectoglaciermelticecapmonitor.domain.port.out.GlacierBedrockIceThicknessNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GlacierBedrockIceThicknessNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GlacierBedrockIceThicknessNodeApplicationService implements ManageGlacierBedrockIceThicknessNodeUseCase {

    private final GlacierBedrockIceThicknessNodeRepositoryPort repositoryPort;

    public GlacierBedrockIceThicknessNodeApplicationService(GlacierBedrockIceThicknessNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GlacierBedrockIceThicknessNode createGlacierBedrockIceThicknessNode(String tenantId, String title, double value) {
        GlacierBedrockIceThicknessNode entity = new GlacierBedrockIceThicknessNode(
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
    public Optional<GlacierBedrockIceThicknessNode> findGlacierBedrockIceThicknessNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GlacierBedrockIceThicknessNode processOptimization(String id, String tenantId) {
        GlacierBedrockIceThicknessNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GlacierBedrockIceThicknessNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
