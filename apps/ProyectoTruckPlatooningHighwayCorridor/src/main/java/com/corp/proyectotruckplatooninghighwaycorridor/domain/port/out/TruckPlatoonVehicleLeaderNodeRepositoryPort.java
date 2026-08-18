package com.corp.proyectotruckplatooninghighwaycorridor.domain.port.out;

import com.corp.proyectotruckplatooninghighwaycorridor.domain.model.TruckPlatoonVehicleLeaderNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface TruckPlatoonVehicleLeaderNodeRepositoryPort {
    TruckPlatoonVehicleLeaderNode save(TruckPlatoonVehicleLeaderNode entity);
    Optional<TruckPlatoonVehicleLeaderNode> findById(String id, String tenantId);
}
