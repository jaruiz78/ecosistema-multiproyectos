package com.corp.proyectomaritimeautonomousfleet.domain.port.in;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAutonomousVesselVoyageUseCase {
    AutonomousVesselVoyage createAutonomousVesselVoyage(String tenantId, String title, double value);
    Optional<AutonomousVesselVoyage> findAutonomousVesselVoyageById(String id, String tenantId);
    AutonomousVesselVoyage processOptimization(String id, String tenantId);
}
