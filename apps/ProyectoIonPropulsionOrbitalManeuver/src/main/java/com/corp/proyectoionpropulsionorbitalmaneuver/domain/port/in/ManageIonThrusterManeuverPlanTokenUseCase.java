package com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.in;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageIonThrusterManeuverPlanTokenUseCase {
    IonThrusterManeuverPlanToken createIonThrusterManeuverPlanToken(String tenantId, String title, double value);
    Optional<IonThrusterManeuverPlanToken> findIonThrusterManeuverPlanTokenById(String id, String tenantId);
    IonThrusterManeuverPlanToken processOptimization(String id, String tenantId);
}
