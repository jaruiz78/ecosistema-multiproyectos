package com.corp.proyectodesertdustairqualitygrid.domain.port.out;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MineralDustAerosolOpticalDepthNodeRepositoryPort {
    MineralDustAerosolOpticalDepthNode save(MineralDustAerosolOpticalDepthNode entity);
    Optional<MineralDustAerosolOpticalDepthNode> findById(String id, String tenantId);
}
