package com.corp.proyectodeepgeothermalenergytwin.domain.port.in;

import com.corp.proyectodeepgeothermalenergytwin.domain.model.GeothermalBoreholeHeatExchangerNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGeothermalBoreholeHeatExchangerNodeUseCase {
    GeothermalBoreholeHeatExchangerNode createGeothermalBoreholeHeatExchangerNode(String tenantId, String title, double value);
    Optional<GeothermalBoreholeHeatExchangerNode> findGeothermalBoreholeHeatExchangerNodeById(String id, String tenantId);
    GeothermalBoreholeHeatExchangerNode processOptimization(String id, String tenantId);
}
