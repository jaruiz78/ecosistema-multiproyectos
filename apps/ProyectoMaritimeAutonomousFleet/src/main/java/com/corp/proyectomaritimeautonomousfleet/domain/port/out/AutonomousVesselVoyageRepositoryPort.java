package com.corp.proyectomaritimeautonomousfleet.domain.port.out;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AutonomousVesselVoyageRepositoryPort {
    AutonomousVesselVoyage save(AutonomousVesselVoyage entity);
    Optional<AutonomousVesselVoyage> findById(String id, String tenantId);
}
