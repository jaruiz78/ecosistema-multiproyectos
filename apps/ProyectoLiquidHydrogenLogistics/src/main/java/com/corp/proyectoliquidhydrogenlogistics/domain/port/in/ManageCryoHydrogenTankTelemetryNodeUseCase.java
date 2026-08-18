package com.corp.proyectoliquidhydrogenlogistics.domain.port.in;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCryoHydrogenTankTelemetryNodeUseCase {
    CryoHydrogenTankTelemetryNode createCryoHydrogenTankTelemetryNode(String tenantId, String title, double value);
    Optional<CryoHydrogenTankTelemetryNode> findCryoHydrogenTankTelemetryNodeById(String id, String tenantId);
    CryoHydrogenTankTelemetryNode processOptimization(String id, String tenantId);
}
