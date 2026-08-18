package com.corp.proyectoevtolurbanaircorridors.domain.port.out;

import com.corp.proyectoevtolurbanaircorridors.domain.model.EvtolFlightTrajectorySlotToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EvtolFlightTrajectorySlotTokenRepositoryPort {
    EvtolFlightTrajectorySlotToken save(EvtolFlightTrajectorySlotToken entity);
    Optional<EvtolFlightTrajectorySlotToken> findById(String id, String tenantId);
}
