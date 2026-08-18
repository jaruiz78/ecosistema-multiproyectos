package com.corp.proyectoquantumgravimetryaquifer.domain.port.out;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GravimetricSubsurfaceDensityNodeRepositoryPort {
    GravimetricSubsurfaceDensityNode save(GravimetricSubsurfaceDensityNode entity);
    Optional<GravimetricSubsurfaceDensityNode> findById(String id, String tenantId);
}
