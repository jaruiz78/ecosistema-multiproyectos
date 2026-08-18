package com.corp.proyectodeepgeothermalenergytwin.domain.port.out;

import com.corp.proyectodeepgeothermalenergytwin.domain.model.GeothermalBoreholeHeatExchangerNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GeothermalBoreholeHeatExchangerNodeRepositoryPort {
    GeothermalBoreholeHeatExchangerNode save(GeothermalBoreholeHeatExchangerNode entity);
    Optional<GeothermalBoreholeHeatExchangerNode> findById(String id, String tenantId);
}
