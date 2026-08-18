package com.corp.proyectodeepspacesolarsailmission.domain.port.in;

import com.corp.proyectodeepspacesolarsailmission.domain.model.SolarSailAttitudeVectorNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSolarSailAttitudeVectorNodeUseCase {
    SolarSailAttitudeVectorNode createSolarSailAttitudeVectorNode(String tenantId, String title, double value);
    Optional<SolarSailAttitudeVectorNode> findSolarSailAttitudeVectorNodeById(String id, String tenantId);
    SolarSailAttitudeVectorNode processOptimization(String id, String tenantId);
}
