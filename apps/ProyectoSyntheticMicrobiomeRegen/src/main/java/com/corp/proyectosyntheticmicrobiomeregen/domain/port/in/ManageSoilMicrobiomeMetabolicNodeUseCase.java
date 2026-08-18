package com.corp.proyectosyntheticmicrobiomeregen.domain.port.in;

import com.corp.proyectosyntheticmicrobiomeregen.domain.model.SoilMicrobiomeMetabolicNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSoilMicrobiomeMetabolicNodeUseCase {
    SoilMicrobiomeMetabolicNode createSoilMicrobiomeMetabolicNode(String tenantId, String title, double value);
    Optional<SoilMicrobiomeMetabolicNode> findSoilMicrobiomeMetabolicNodeById(String id, String tenantId);
    SoilMicrobiomeMetabolicNode processOptimization(String id, String tenantId);
}
