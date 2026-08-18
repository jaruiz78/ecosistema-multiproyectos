package com.corp.proyectotsunamiearlywarningsystem.domain.port.out;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface TsunamiWaveformPressureSensorNodeRepositoryPort {
    TsunamiWaveformPressureSensorNode save(TsunamiWaveformPressureSensorNode entity);
    Optional<TsunamiWaveformPressureSensorNode> findById(String id, String tenantId);
}
