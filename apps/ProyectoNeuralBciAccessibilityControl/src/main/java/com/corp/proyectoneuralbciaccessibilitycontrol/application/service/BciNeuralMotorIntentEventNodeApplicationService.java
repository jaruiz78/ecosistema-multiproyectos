package com.corp.proyectoneuralbciaccessibilitycontrol.application.service;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.in.ManageBciNeuralMotorIntentEventNodeUseCase;
import com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.out.BciNeuralMotorIntentEventNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BciNeuralMotorIntentEventNode.
 */
@Service
public class BciNeuralMotorIntentEventNodeApplicationService implements ManageBciNeuralMotorIntentEventNodeUseCase {

    private final BciNeuralMotorIntentEventNodeRepositoryPort repositoryPort;

    public BciNeuralMotorIntentEventNodeApplicationService(BciNeuralMotorIntentEventNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BciNeuralMotorIntentEventNode createBciNeuralMotorIntentEventNode(String tenantId, String title, double value) {
        BciNeuralMotorIntentEventNode entity = new BciNeuralMotorIntentEventNode(
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
    public Optional<BciNeuralMotorIntentEventNode> findBciNeuralMotorIntentEventNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BciNeuralMotorIntentEventNode processOptimization(String id, String tenantId) {
        BciNeuralMotorIntentEventNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BciNeuralMotorIntentEventNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
