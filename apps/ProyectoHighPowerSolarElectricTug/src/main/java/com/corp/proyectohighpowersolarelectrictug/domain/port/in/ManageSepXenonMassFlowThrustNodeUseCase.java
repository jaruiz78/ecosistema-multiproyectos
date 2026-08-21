package com.corp.proyectohighpowersolarelectrictug.domain.port.in;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageSepXenonMassFlowThrustNodeUseCase {
    SepXenonMassFlowThrustNode createSepXenonMassFlowThrustNode(String tenantId, String title, double value);
    Optional<SepXenonMassFlowThrustNode> findSepXenonMassFlowThrustNodeById(String id, String tenantId);
    SepXenonMassFlowThrustNode processOptimization(String id, String tenantId);
}
