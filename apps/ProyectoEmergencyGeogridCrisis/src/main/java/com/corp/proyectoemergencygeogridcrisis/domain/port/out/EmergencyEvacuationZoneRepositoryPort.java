package com.corp.proyectoemergencygeogridcrisis.domain.port.out;

import com.corp.proyectoemergencygeogridcrisis.domain.model.EmergencyEvacuationZone;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EmergencyEvacuationZoneRepositoryPort {
    EmergencyEvacuationZone save(EmergencyEvacuationZone entity);
    Optional<EmergencyEvacuationZone> findById(String id, String tenantId);
}
