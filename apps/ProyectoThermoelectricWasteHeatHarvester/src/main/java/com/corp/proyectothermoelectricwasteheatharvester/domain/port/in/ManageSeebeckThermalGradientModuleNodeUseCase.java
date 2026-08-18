package com.corp.proyectothermoelectricwasteheatharvester.domain.port.in;

import com.corp.proyectothermoelectricwasteheatharvester.domain.model.SeebeckThermalGradientModuleNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSeebeckThermalGradientModuleNodeUseCase {
    SeebeckThermalGradientModuleNode createSeebeckThermalGradientModuleNode(String tenantId, String title, double value);
    Optional<SeebeckThermalGradientModuleNode> findSeebeckThermalGradientModuleNodeById(String id, String tenantId);
    SeebeckThermalGradientModuleNode processOptimization(String id, String tenantId);
}
