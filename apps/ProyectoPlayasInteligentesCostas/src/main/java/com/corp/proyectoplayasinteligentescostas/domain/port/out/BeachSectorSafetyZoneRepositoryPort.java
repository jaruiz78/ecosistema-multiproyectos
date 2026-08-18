package com.corp.proyectoplayasinteligentescostas.domain.port.out;

import com.corp.proyectoplayasinteligentescostas.domain.model.BeachSectorSafetyZone;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BeachSectorSafetyZoneRepositoryPort {
    BeachSectorSafetyZone save(BeachSectorSafetyZone entity);
    Optional<BeachSectorSafetyZone> findById(String id, String tenantId);
}
