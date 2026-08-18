package com.corp.proyectoexosomenanovesicletherapeutics.domain.port.out;

import com.corp.proyectoexosomenanovesicletherapeutics.domain.model.ExosomeSurfaceMarkerTropismNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ExosomeSurfaceMarkerTropismNodeRepositoryPort {
    ExosomeSurfaceMarkerTropismNode save(ExosomeSurfaceMarkerTropismNode entity);
    Optional<ExosomeSurfaceMarkerTropismNode> findById(String id, String tenantId);
}
