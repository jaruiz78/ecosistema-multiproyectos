package com.corp.proyectocellfreeproteinbiomanufacturing.application.service;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.in.ManageCfpsReactionYieldBatchTokenUseCase;
import com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.out.CfpsReactionYieldBatchTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CfpsReactionYieldBatchToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CfpsReactionYieldBatchTokenApplicationService implements ManageCfpsReactionYieldBatchTokenUseCase {

    private final CfpsReactionYieldBatchTokenRepositoryPort repositoryPort;

    public CfpsReactionYieldBatchTokenApplicationService(CfpsReactionYieldBatchTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CfpsReactionYieldBatchToken createCfpsReactionYieldBatchToken(String tenantId, String title, double value) {
        CfpsReactionYieldBatchToken entity = new CfpsReactionYieldBatchToken(
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
    public Optional<CfpsReactionYieldBatchToken> findCfpsReactionYieldBatchTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CfpsReactionYieldBatchToken processOptimization(String id, String tenantId) {
        CfpsReactionYieldBatchToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CfpsReactionYieldBatchToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
