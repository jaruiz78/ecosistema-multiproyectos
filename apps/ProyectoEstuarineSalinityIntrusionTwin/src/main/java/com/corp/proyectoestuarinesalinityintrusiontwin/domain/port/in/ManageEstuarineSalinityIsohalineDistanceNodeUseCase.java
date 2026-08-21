package com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.in;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageEstuarineSalinityIsohalineDistanceNodeUseCase {
    EstuarineSalinityIsohalineDistanceNode createEstuarineSalinityIsohalineDistanceNode(String tenantId, String title, double value);
    Optional<EstuarineSalinityIsohalineDistanceNode> findEstuarineSalinityIsohalineDistanceNodeById(String id, String tenantId);
    EstuarineSalinityIsohalineDistanceNode processOptimization(String id, String tenantId);
}
