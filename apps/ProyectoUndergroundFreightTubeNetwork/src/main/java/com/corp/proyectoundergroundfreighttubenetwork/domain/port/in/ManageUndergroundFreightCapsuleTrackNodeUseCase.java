package com.corp.proyectoundergroundfreighttubenetwork.domain.port.in;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageUndergroundFreightCapsuleTrackNodeUseCase {
    UndergroundFreightCapsuleTrackNode createUndergroundFreightCapsuleTrackNode(String tenantId, String title, double value);
    Optional<UndergroundFreightCapsuleTrackNode> findUndergroundFreightCapsuleTrackNodeById(String id, String tenantId);
    UndergroundFreightCapsuleTrackNode processOptimization(String id, String tenantId);
}
