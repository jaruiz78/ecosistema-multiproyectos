package com.corp.proyectocoralreefecosystempreserve.domain.port.in;

import com.corp.proyectocoralreefecosystempreserve.domain.model.CoralBleachingDegreeWeekNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCoralBleachingDegreeWeekNodeUseCase {
    CoralBleachingDegreeWeekNode createCoralBleachingDegreeWeekNode(String tenantId, String title, double value);
    Optional<CoralBleachingDegreeWeekNode> findCoralBleachingDegreeWeekNodeById(String id, String tenantId);
    CoralBleachingDegreeWeekNode processOptimization(String id, String tenantId);
}
