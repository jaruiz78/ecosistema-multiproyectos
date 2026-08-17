package com.corp.proyectocriticalsupplyrisk.domain.model;

import java.io.Serializable;

/**
 * Nodo de red de suministro global (refinería, fundición, puerto estratégico, fábrica Tier-1).
 */
public record SupplyChainNode(
        String nodeId,
        String materialType, // ej: "LITHIUM_HYDROXIDE", "GALLIUM", "NEODYMIUM"
        double dailyCapacityTonnes,
        double currentStockTonnes,
        double geopoliticalRiskIndex, // 0.0 a 1.0
        boolean isChokepoint
) implements Serializable {

    public SupplyChainNode simulateDisruption(double disruptionSeverityPct) {
        double adjustedCapacity = dailyCapacityTonnes * (1.0 - Math.clamp(disruptionSeverityPct, 0.0, 1.0));
        double adjustedRisk = Math.min(1.0, geopoliticalRiskIndex + disruptionSeverityPct * 0.5);

        return new SupplyChainNode(nodeId, materialType, adjustedCapacity, currentStockTonnes, adjustedRisk, isChokepoint);
    }
}
