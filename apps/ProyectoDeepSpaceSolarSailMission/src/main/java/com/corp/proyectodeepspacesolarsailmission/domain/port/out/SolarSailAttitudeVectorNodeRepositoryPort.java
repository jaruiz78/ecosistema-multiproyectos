package com.corp.proyectodeepspacesolarsailmission.domain.port.out;

import com.corp.proyectodeepspacesolarsailmission.domain.model.SolarSailAttitudeVectorNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SolarSailAttitudeVectorNodeRepositoryPort {
    SolarSailAttitudeVectorNode save(SolarSailAttitudeVectorNode entity);
    Optional<SolarSailAttitudeVectorNode> findById(String id, String tenantId);
}
