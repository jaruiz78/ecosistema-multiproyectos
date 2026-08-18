package com.corp.proyectomantlegeodynamicssimulator.domain.port.in;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMantlePlumeThermalUpwellingNodeUseCase {
    MantlePlumeThermalUpwellingNode createMantlePlumeThermalUpwellingNode(String tenantId, String title, double value);
    Optional<MantlePlumeThermalUpwellingNode> findMantlePlumeThermalUpwellingNodeById(String id, String tenantId);
    MantlePlumeThermalUpwellingNode processOptimization(String id, String tenantId);
}
