package com.corp.proyectosmartdestinationdti.domain.port.in;

import com.corp.proyectosmartdestinationdti.domain.model.DestinationCapacityZone;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDestinationCapacityZoneUseCase {
    DestinationCapacityZone createDestinationCapacityZone(String tenantId, String title, double value);
    Optional<DestinationCapacityZone> findDestinationCapacityZoneById(String id, String tenantId);
    DestinationCapacityZone processOptimization(String id, String tenantId);
}
