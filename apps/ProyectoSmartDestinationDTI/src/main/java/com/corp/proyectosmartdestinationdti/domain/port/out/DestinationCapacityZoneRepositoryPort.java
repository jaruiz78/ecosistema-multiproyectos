package com.corp.proyectosmartdestinationdti.domain.port.out;

import com.corp.proyectosmartdestinationdti.domain.model.DestinationCapacityZone;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DestinationCapacityZoneRepositoryPort {
    DestinationCapacityZone save(DestinationCapacityZone entity);
    Optional<DestinationCapacityZone> findById(String id, String tenantId);
}
