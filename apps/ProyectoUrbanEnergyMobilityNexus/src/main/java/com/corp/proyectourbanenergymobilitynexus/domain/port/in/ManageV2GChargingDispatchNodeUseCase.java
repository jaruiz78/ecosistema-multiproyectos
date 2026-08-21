package com.corp.proyectourbanenergymobilitynexus.domain.port.in;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageV2GChargingDispatchNodeUseCase {
    V2GChargingDispatchNode createV2GChargingDispatchNode(String tenantId, String title, double value);
    Optional<V2GChargingDispatchNode> findV2GChargingDispatchNodeById(String id, String tenantId);
    V2GChargingDispatchNode processOptimization(String id, String tenantId);
}
