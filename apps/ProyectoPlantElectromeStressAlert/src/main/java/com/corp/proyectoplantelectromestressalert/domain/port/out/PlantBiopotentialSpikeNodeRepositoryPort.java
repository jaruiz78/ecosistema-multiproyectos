package com.corp.proyectoplantelectromestressalert.domain.port.out;

import com.corp.proyectoplantelectromestressalert.domain.model.PlantBiopotentialSpikeNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlantBiopotentialSpikeNodeRepositoryPort {
    PlantBiopotentialSpikeNode save(PlantBiopotentialSpikeNode entity);
    Optional<PlantBiopotentialSpikeNode> findById(String id, String tenantId);
}
