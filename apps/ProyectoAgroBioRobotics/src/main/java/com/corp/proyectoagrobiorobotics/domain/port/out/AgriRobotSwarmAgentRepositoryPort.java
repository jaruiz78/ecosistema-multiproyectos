package com.corp.proyectoagrobiorobotics.domain.port.out;

import com.corp.proyectoagrobiorobotics.domain.model.AgriRobotSwarmAgent;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AgriRobotSwarmAgentRepositoryPort {
    AgriRobotSwarmAgent save(AgriRobotSwarmAgent entity);
    Optional<AgriRobotSwarmAgent> findById(String id, String tenantId);
}
