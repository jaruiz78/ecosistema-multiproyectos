package com.corp.proyectoatmosphericwaterharvesting.domain.port.out;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MofWaterAdsorptionChamberNodeRepositoryPort {
    MofWaterAdsorptionChamberNode save(MofWaterAdsorptionChamberNode entity);
    Optional<MofWaterAdsorptionChamberNode> findById(String id, String tenantId);
}
