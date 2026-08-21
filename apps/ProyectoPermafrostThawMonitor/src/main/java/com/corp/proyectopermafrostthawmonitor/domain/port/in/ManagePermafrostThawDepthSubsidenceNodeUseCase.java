package com.corp.proyectopermafrostthawmonitor.domain.port.in;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePermafrostThawDepthSubsidenceNodeUseCase {
    PermafrostThawDepthSubsidenceNode createPermafrostThawDepthSubsidenceNode(String tenantId, String title, double value);
    Optional<PermafrostThawDepthSubsidenceNode> findPermafrostThawDepthSubsidenceNodeById(String id, String tenantId);
    PermafrostThawDepthSubsidenceNode processOptimization(String id, String tenantId);
}
