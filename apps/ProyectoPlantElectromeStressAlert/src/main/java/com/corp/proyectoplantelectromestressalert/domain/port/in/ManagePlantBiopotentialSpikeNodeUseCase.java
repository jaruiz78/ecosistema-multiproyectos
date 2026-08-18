package com.corp.proyectoplantelectromestressalert.domain.port.in;

import com.corp.proyectoplantelectromestressalert.domain.model.PlantBiopotentialSpikeNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlantBiopotentialSpikeNodeUseCase {
    PlantBiopotentialSpikeNode createPlantBiopotentialSpikeNode(String tenantId, String title, double value);
    Optional<PlantBiopotentialSpikeNode> findPlantBiopotentialSpikeNodeById(String id, String tenantId);
    PlantBiopotentialSpikeNode processOptimization(String id, String tenantId);
}
