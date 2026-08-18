package com.corp.proyectootecmarinecleanenergy.domain.port.out;

import com.corp.proyectootecmarinecleanenergy.domain.model.OtecThermalGradientTurbineNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface OtecThermalGradientTurbineNodeRepositoryPort {
    OtecThermalGradientTurbineNode save(OtecThermalGradientTurbineNode entity);
    Optional<OtecThermalGradientTurbineNode> findById(String id, String tenantId);
}
