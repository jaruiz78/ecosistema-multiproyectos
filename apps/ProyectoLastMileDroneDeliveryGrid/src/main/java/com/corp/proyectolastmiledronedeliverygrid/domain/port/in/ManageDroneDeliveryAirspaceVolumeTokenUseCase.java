package com.corp.proyectolastmiledronedeliverygrid.domain.port.in;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageDroneDeliveryAirspaceVolumeTokenUseCase {
    DroneDeliveryAirspaceVolumeToken createDroneDeliveryAirspaceVolumeToken(String tenantId, String title, double value);
    Optional<DroneDeliveryAirspaceVolumeToken> findDroneDeliveryAirspaceVolumeTokenById(String id, String tenantId);
    DroneDeliveryAirspaceVolumeToken processOptimization(String id, String tenantId);
}
