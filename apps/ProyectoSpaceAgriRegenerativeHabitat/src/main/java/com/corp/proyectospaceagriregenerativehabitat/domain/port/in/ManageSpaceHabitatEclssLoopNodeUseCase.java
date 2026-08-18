package com.corp.proyectospaceagriregenerativehabitat.domain.port.in;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpaceHabitatEclssLoopNodeUseCase {
    SpaceHabitatEclssLoopNode createSpaceHabitatEclssLoopNode(String tenantId, String title, double value);
    Optional<SpaceHabitatEclssLoopNode> findSpaceHabitatEclssLoopNodeById(String id, String tenantId);
    SpaceHabitatEclssLoopNode processOptimization(String id, String tenantId);
}
