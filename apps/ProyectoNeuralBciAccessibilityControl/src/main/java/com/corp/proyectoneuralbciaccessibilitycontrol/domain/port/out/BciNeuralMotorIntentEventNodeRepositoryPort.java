package com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.out;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BciNeuralMotorIntentEventNodeRepositoryPort {
    BciNeuralMotorIntentEventNode save(BciNeuralMotorIntentEventNode entity);
    Optional<BciNeuralMotorIntentEventNode> findById(String id, String tenantId);
}
