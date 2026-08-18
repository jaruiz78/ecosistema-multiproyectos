package com.corp.proyectoagrobiorobotics.application.service;

import com.corp.proyectoagrobiorobotics.domain.model.AgriRobotSwarmAgent;
import com.corp.proyectoagrobiorobotics.domain.port.in.ManageAgriRobotSwarmAgentUseCase;
import com.corp.proyectoagrobiorobotics.domain.port.out.AgriRobotSwarmAgentRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AgriRobotSwarmAgent.
 */
@Service
public class AgriRobotSwarmAgentApplicationService implements ManageAgriRobotSwarmAgentUseCase {

    private final AgriRobotSwarmAgentRepositoryPort repositoryPort;

    public AgriRobotSwarmAgentApplicationService(AgriRobotSwarmAgentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AgriRobotSwarmAgent createAgriRobotSwarmAgent(String tenantId, String title, double value) {
        AgriRobotSwarmAgent entity = new AgriRobotSwarmAgent(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<AgriRobotSwarmAgent> findAgriRobotSwarmAgentById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AgriRobotSwarmAgent processOptimization(String id, String tenantId) {
        AgriRobotSwarmAgent existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AgriRobotSwarmAgent optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
