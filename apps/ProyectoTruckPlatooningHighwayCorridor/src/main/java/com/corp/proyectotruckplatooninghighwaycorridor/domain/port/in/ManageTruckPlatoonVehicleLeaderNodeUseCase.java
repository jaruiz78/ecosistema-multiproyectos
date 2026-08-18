package com.corp.proyectotruckplatooninghighwaycorridor.domain.port.in;

import com.corp.proyectotruckplatooninghighwaycorridor.domain.model.TruckPlatoonVehicleLeaderNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageTruckPlatoonVehicleLeaderNodeUseCase {
    TruckPlatoonVehicleLeaderNode createTruckPlatoonVehicleLeaderNode(String tenantId, String title, double value);
    Optional<TruckPlatoonVehicleLeaderNode> findTruckPlatoonVehicleLeaderNodeById(String id, String tenantId);
    TruckPlatoonVehicleLeaderNode processOptimization(String id, String tenantId);
}
