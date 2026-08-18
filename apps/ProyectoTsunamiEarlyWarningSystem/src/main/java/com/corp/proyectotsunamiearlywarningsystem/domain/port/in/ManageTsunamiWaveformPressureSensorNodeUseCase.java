package com.corp.proyectotsunamiearlywarningsystem.domain.port.in;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageTsunamiWaveformPressureSensorNodeUseCase {
    TsunamiWaveformPressureSensorNode createTsunamiWaveformPressureSensorNode(String tenantId, String title, double value);
    Optional<TsunamiWaveformPressureSensorNode> findTsunamiWaveformPressureSensorNodeById(String id, String tenantId);
    TsunamiWaveformPressureSensorNode processOptimization(String id, String tenantId);
}
