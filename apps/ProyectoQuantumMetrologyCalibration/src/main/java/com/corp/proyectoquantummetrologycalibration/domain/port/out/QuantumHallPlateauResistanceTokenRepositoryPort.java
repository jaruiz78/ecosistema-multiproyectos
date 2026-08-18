package com.corp.proyectoquantummetrologycalibration.domain.port.out;

import com.corp.proyectoquantummetrologycalibration.domain.model.QuantumHallPlateauResistanceToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuantumHallPlateauResistanceTokenRepositoryPort {
    QuantumHallPlateauResistanceToken save(QuantumHallPlateauResistanceToken entity);
    Optional<QuantumHallPlateauResistanceToken> findById(String id, String tenantId);
}
