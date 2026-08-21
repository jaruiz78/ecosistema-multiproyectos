package com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.in;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageBciNeuralMotorIntentEventNodeUseCase {
    BciNeuralMotorIntentEventNode createBciNeuralMotorIntentEventNode(String tenantId, String title, double value);
    Optional<BciNeuralMotorIntentEventNode> findBciNeuralMotorIntentEventNodeById(String id, String tenantId);
    BciNeuralMotorIntentEventNode processOptimization(String id, String tenantId);
}
