package com.corp.proyectoliquidhydrogenlogistics.domain.port.out;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CryoHydrogenTankTelemetryNodeRepositoryPort {
    CryoHydrogenTankTelemetryNode save(CryoHydrogenTankTelemetryNode entity);
    Optional<CryoHydrogenTankTelemetryNode> findById(String id, String tenantId);
}
