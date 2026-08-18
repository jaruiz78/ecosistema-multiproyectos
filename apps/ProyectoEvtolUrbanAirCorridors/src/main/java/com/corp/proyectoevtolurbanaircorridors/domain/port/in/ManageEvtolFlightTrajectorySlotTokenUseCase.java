package com.corp.proyectoevtolurbanaircorridors.domain.port.in;

import com.corp.proyectoevtolurbanaircorridors.domain.model.EvtolFlightTrajectorySlotToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEvtolFlightTrajectorySlotTokenUseCase {
    EvtolFlightTrajectorySlotToken createEvtolFlightTrajectorySlotToken(String tenantId, String title, double value);
    Optional<EvtolFlightTrajectorySlotToken> findEvtolFlightTrajectorySlotTokenById(String id, String tenantId);
    EvtolFlightTrajectorySlotToken processOptimization(String id, String tenantId);
}
