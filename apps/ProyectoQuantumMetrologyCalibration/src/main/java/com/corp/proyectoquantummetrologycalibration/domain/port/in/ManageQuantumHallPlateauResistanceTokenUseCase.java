package com.corp.proyectoquantummetrologycalibration.domain.port.in;

import com.corp.proyectoquantummetrologycalibration.domain.model.QuantumHallPlateauResistanceToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuantumHallPlateauResistanceTokenUseCase {
    QuantumHallPlateauResistanceToken createQuantumHallPlateauResistanceToken(String tenantId, String title, double value);
    Optional<QuantumHallPlateauResistanceToken> findQuantumHallPlateauResistanceTokenById(String id, String tenantId);
    QuantumHallPlateauResistanceToken processOptimization(String id, String tenantId);
}
