package com.corp.proyectootecmarinecleanenergy.domain.port.in;

import com.corp.proyectootecmarinecleanenergy.domain.model.OtecThermalGradientTurbineNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageOtecThermalGradientTurbineNodeUseCase {
    OtecThermalGradientTurbineNode createOtecThermalGradientTurbineNode(String tenantId, String title, double value);
    Optional<OtecThermalGradientTurbineNode> findOtecThermalGradientTurbineNodeById(String id, String tenantId);
    OtecThermalGradientTurbineNode processOptimization(String id, String tenantId);
}
