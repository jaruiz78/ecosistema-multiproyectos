package com.corp.proyectoemergencygeogridcrisis.domain.port.in;

import com.corp.proyectoemergencygeogridcrisis.domain.model.EmergencyEvacuationZone;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEmergencyEvacuationZoneUseCase {
    EmergencyEvacuationZone createEmergencyEvacuationZone(String tenantId, String title, double value);
    Optional<EmergencyEvacuationZone> findEmergencyEvacuationZoneById(String id, String tenantId);
    EmergencyEvacuationZone processOptimization(String id, String tenantId);
}
