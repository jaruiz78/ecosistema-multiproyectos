package com.corp.proyectoseismicresilienceinfrastructure.domain.port.out;

import com.corp.proyectoseismicresilienceinfrastructure.domain.model.SeismicBaseIsolatorDisplacementNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SeismicBaseIsolatorDisplacementNodeRepositoryPort {
    SeismicBaseIsolatorDisplacementNode save(SeismicBaseIsolatorDisplacementNode entity);
    Optional<SeismicBaseIsolatorDisplacementNode> findById(String id, String tenantId);
}
