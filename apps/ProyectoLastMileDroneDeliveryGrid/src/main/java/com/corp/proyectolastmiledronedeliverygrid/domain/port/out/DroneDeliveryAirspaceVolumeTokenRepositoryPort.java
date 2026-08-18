package com.corp.proyectolastmiledronedeliverygrid.domain.port.out;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DroneDeliveryAirspaceVolumeTokenRepositoryPort {
    DroneDeliveryAirspaceVolumeToken save(DroneDeliveryAirspaceVolumeToken entity);
    Optional<DroneDeliveryAirspaceVolumeToken> findById(String id, String tenantId);
}
