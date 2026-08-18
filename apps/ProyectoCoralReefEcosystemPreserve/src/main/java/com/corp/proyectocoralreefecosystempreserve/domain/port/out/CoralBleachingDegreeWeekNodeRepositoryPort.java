package com.corp.proyectocoralreefecosystempreserve.domain.port.out;

import com.corp.proyectocoralreefecosystempreserve.domain.model.CoralBleachingDegreeWeekNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CoralBleachingDegreeWeekNodeRepositoryPort {
    CoralBleachingDegreeWeekNode save(CoralBleachingDegreeWeekNode entity);
    Optional<CoralBleachingDegreeWeekNode> findById(String id, String tenantId);
}
