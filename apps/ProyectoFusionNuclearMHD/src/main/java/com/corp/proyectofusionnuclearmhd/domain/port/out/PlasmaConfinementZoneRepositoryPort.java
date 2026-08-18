package com.corp.proyectofusionnuclearmhd.domain.port.out;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlasmaConfinementZoneRepositoryPort {
    PlasmaConfinementZone save(PlasmaConfinementZone entity);
    Optional<PlasmaConfinementZone> findById(String id, String tenantId);
}
