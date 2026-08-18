package com.corp.proyectothermoelectricwasteheatharvester.domain.port.out;

import com.corp.proyectothermoelectricwasteheatharvester.domain.model.SeebeckThermalGradientModuleNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SeebeckThermalGradientModuleNodeRepositoryPort {
    SeebeckThermalGradientModuleNode save(SeebeckThermalGradientModuleNode entity);
    Optional<SeebeckThermalGradientModuleNode> findById(String id, String tenantId);
}
