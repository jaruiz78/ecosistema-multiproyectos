package com.corp.proyectoconcentratedliquidityamm.application.service;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import com.corp.proyectoconcentratedliquidityamm.domain.port.in.ManageAmmLiquidityPoolPositionNodeUseCase;
import com.corp.proyectoconcentratedliquidityamm.domain.port.out.AmmLiquidityPoolPositionNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AmmLiquidityPoolPositionNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AmmLiquidityPoolPositionNodeApplicationService implements ManageAmmLiquidityPoolPositionNodeUseCase {

    private final AmmLiquidityPoolPositionNodeRepositoryPort repositoryPort;

    public AmmLiquidityPoolPositionNodeApplicationService(AmmLiquidityPoolPositionNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AmmLiquidityPoolPositionNode createAmmLiquidityPoolPositionNode(String tenantId, String title, double value) {
        AmmLiquidityPoolPositionNode entity = new AmmLiquidityPoolPositionNode(
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
    public Optional<AmmLiquidityPoolPositionNode> findAmmLiquidityPoolPositionNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AmmLiquidityPoolPositionNode processOptimization(String id, String tenantId) {
        AmmLiquidityPoolPositionNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AmmLiquidityPoolPositionNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
