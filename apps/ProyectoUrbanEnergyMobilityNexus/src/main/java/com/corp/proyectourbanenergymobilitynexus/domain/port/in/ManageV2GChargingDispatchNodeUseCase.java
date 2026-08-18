package com.corp.proyectourbanenergymobilitynexus.domain.port.in;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageV2GChargingDispatchNodeUseCase {
    V2GChargingDispatchNode createV2GChargingDispatchNode(String tenantId, String title, double value);
    Optional<V2GChargingDispatchNode> findV2GChargingDispatchNodeById(String id, String tenantId);
    V2GChargingDispatchNode processOptimization(String id, String tenantId);
}
