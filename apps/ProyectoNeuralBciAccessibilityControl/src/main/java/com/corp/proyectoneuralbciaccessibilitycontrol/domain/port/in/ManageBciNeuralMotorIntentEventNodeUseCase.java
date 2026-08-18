package com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.in;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBciNeuralMotorIntentEventNodeUseCase {
    BciNeuralMotorIntentEventNode createBciNeuralMotorIntentEventNode(String tenantId, String title, double value);
    Optional<BciNeuralMotorIntentEventNode> findBciNeuralMotorIntentEventNodeById(String id, String tenantId);
    BciNeuralMotorIntentEventNode processOptimization(String id, String tenantId);
}
