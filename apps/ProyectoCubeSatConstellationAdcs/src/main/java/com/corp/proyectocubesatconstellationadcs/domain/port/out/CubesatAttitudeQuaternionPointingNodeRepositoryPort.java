package com.corp.proyectocubesatconstellationadcs.domain.port.out;

import com.corp.proyectocubesatconstellationadcs.domain.model.CubesatAttitudeQuaternionPointingNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CubesatAttitudeQuaternionPointingNodeRepositoryPort {
    CubesatAttitudeQuaternionPointingNode save(CubesatAttitudeQuaternionPointingNode entity);
    Optional<CubesatAttitudeQuaternionPointingNode> findById(String id, String tenantId);
}
