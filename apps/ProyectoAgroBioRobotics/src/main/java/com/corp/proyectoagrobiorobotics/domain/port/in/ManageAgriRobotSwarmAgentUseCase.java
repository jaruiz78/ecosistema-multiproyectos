package com.corp.proyectoagrobiorobotics.domain.port.in;

import com.corp.proyectoagrobiorobotics.domain.model.AgriRobotSwarmAgent;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAgriRobotSwarmAgentUseCase {
    AgriRobotSwarmAgent createAgriRobotSwarmAgent(String tenantId, String title, double value);
    Optional<AgriRobotSwarmAgent> findAgriRobotSwarmAgentById(String id, String tenantId);
    AgriRobotSwarmAgent processOptimization(String id, String tenantId);
}
