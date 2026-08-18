package com.corp.proyectofusionnuclearmhd.domain.port.in;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlasmaConfinementZoneUseCase {
    PlasmaConfinementZone createPlasmaConfinementZone(String tenantId, String title, double value);
    Optional<PlasmaConfinementZone> findPlasmaConfinementZoneById(String id, String tenantId);
    PlasmaConfinementZone processOptimization(String id, String tenantId);
}
