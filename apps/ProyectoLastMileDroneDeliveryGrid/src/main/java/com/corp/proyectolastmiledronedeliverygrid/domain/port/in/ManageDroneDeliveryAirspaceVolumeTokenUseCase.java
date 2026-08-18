package com.corp.proyectolastmiledronedeliverygrid.domain.port.in;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDroneDeliveryAirspaceVolumeTokenUseCase {
    DroneDeliveryAirspaceVolumeToken createDroneDeliveryAirspaceVolumeToken(String tenantId, String title, double value);
    Optional<DroneDeliveryAirspaceVolumeToken> findDroneDeliveryAirspaceVolumeTokenById(String id, String tenantId);
    DroneDeliveryAirspaceVolumeToken processOptimization(String id, String tenantId);
}
