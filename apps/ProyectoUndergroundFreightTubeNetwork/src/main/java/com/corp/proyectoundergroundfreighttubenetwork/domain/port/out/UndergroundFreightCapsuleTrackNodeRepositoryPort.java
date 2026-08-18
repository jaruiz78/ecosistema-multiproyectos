package com.corp.proyectoundergroundfreighttubenetwork.domain.port.out;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface UndergroundFreightCapsuleTrackNodeRepositoryPort {
    UndergroundFreightCapsuleTrackNode save(UndergroundFreightCapsuleTrackNode entity);
    Optional<UndergroundFreightCapsuleTrackNode> findById(String id, String tenantId);
}
