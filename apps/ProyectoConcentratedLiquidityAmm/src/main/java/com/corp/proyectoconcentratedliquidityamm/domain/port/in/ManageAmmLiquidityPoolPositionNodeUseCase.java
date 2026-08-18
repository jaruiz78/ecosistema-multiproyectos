package com.corp.proyectoconcentratedliquidityamm.domain.port.in;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAmmLiquidityPoolPositionNodeUseCase {
    AmmLiquidityPoolPositionNode createAmmLiquidityPoolPositionNode(String tenantId, String title, double value);
    Optional<AmmLiquidityPoolPositionNode> findAmmLiquidityPoolPositionNodeById(String id, String tenantId);
    AmmLiquidityPoolPositionNode processOptimization(String id, String tenantId);
}
