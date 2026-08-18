package com.corp.proyectodistrictheatingcoolingtwin.domain.port.in;

import com.corp.proyectodistrictheatingcoolingtwin.domain.model.DistrictThermalSubstationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDistrictThermalSubstationNodeUseCase {
    DistrictThermalSubstationNode createDistrictThermalSubstationNode(String tenantId, String title, double value);
    Optional<DistrictThermalSubstationNode> findDistrictThermalSubstationNodeById(String id, String tenantId);
    DistrictThermalSubstationNode processOptimization(String id, String tenantId);
}
