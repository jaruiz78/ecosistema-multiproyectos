package com.corp.proyectostratosphericaerosollidarnetwork.domain.port.out;

import com.corp.proyectostratosphericaerosollidarnetwork.domain.model.AerosolBackscatterExtinctionProfileNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AerosolBackscatterExtinctionProfileNodeRepositoryPort {
    AerosolBackscatterExtinctionProfileNode save(AerosolBackscatterExtinctionProfileNode entity);
    Optional<AerosolBackscatterExtinctionProfileNode> findById(String id, String tenantId);
}
