package com.corp.proyectoquantumgravimetryaquifer.domain.port.in;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGravimetricSubsurfaceDensityNodeUseCase {
    GravimetricSubsurfaceDensityNode createGravimetricSubsurfaceDensityNode(String tenantId, String title, double value);
    Optional<GravimetricSubsurfaceDensityNode> findGravimetricSubsurfaceDensityNodeById(String id, String tenantId);
    GravimetricSubsurfaceDensityNode processOptimization(String id, String tenantId);
}
