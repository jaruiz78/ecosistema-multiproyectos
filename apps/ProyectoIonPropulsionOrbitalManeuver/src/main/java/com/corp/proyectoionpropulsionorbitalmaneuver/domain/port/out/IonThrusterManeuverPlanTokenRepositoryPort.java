package com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.out;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface IonThrusterManeuverPlanTokenRepositoryPort {
    IonThrusterManeuverPlanToken save(IonThrusterManeuverPlanToken entity);
    Optional<IonThrusterManeuverPlanToken> findById(String id, String tenantId);
}
