package com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.out;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface IonThrusterManeuverPlanTokenRepositoryPort {
    IonThrusterManeuverPlanToken save(IonThrusterManeuverPlanToken entity);
    Optional<IonThrusterManeuverPlanToken> findById(String id, String tenantId);
}
