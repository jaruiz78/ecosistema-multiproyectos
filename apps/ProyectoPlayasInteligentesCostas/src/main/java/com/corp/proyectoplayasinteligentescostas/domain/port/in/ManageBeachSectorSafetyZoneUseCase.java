package com.corp.proyectoplayasinteligentescostas.domain.port.in;

import com.corp.proyectoplayasinteligentescostas.domain.model.BeachSectorSafetyZone;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBeachSectorSafetyZoneUseCase {
    BeachSectorSafetyZone createBeachSectorSafetyZone(String tenantId, String title, double value);
    Optional<BeachSectorSafetyZone> findBeachSectorSafetyZoneById(String id, String tenantId);
    BeachSectorSafetyZone processOptimization(String id, String tenantId);
}
