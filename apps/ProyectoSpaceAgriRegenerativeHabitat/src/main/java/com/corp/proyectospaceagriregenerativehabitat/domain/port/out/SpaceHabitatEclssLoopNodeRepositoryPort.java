package com.corp.proyectospaceagriregenerativehabitat.domain.port.out;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpaceHabitatEclssLoopNodeRepositoryPort {
    SpaceHabitatEclssLoopNode save(SpaceHabitatEclssLoopNode entity);
    Optional<SpaceHabitatEclssLoopNode> findById(String id, String tenantId);
}
