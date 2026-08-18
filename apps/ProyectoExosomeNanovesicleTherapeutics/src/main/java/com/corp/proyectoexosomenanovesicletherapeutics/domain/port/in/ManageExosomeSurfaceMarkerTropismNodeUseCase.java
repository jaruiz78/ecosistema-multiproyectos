package com.corp.proyectoexosomenanovesicletherapeutics.domain.port.in;

import com.corp.proyectoexosomenanovesicletherapeutics.domain.model.ExosomeSurfaceMarkerTropismNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageExosomeSurfaceMarkerTropismNodeUseCase {
    ExosomeSurfaceMarkerTropismNode createExosomeSurfaceMarkerTropismNode(String tenantId, String title, double value);
    Optional<ExosomeSurfaceMarkerTropismNode> findExosomeSurfaceMarkerTropismNodeById(String id, String tenantId);
    ExosomeSurfaceMarkerTropismNode processOptimization(String id, String tenantId);
}
