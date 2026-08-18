package com.corp.proyectourbanenergymobilitynexus.domain.port.out;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface V2GChargingDispatchNodeRepositoryPort {
    V2GChargingDispatchNode save(V2GChargingDispatchNode entity);
    Optional<V2GChargingDispatchNode> findById(String id, String tenantId);
}
