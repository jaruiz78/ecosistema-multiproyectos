package com.corp.proyectodistrictheatingcoolingtwin.domain.port.out;

import com.corp.proyectodistrictheatingcoolingtwin.domain.model.DistrictThermalSubstationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DistrictThermalSubstationNodeRepositoryPort {
    DistrictThermalSubstationNode save(DistrictThermalSubstationNode entity);
    Optional<DistrictThermalSubstationNode> findById(String id, String tenantId);
}
