package com.corp.proyectospaceagriregenerativehabitat.domain.port.in;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageSpaceHabitatEclssLoopNodeUseCase {
    SpaceHabitatEclssLoopNode createSpaceHabitatEclssLoopNode(String tenantId, String title, double value);
    Optional<SpaceHabitatEclssLoopNode> findSpaceHabitatEclssLoopNodeById(String id, String tenantId);
    SpaceHabitatEclssLoopNode processOptimization(String id, String tenantId);
}
