package com.corp.proyectoconcentratedliquidityamm.domain.port.out;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AmmLiquidityPoolPositionNodeRepositoryPort {
    AmmLiquidityPoolPositionNode save(AmmLiquidityPoolPositionNode entity);
    Optional<AmmLiquidityPoolPositionNode> findById(String id, String tenantId);
}
