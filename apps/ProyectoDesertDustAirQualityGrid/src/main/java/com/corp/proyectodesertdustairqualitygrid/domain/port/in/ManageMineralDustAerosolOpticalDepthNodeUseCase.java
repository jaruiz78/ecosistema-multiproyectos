package com.corp.proyectodesertdustairqualitygrid.domain.port.in;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMineralDustAerosolOpticalDepthNodeUseCase {
    MineralDustAerosolOpticalDepthNode createMineralDustAerosolOpticalDepthNode(String tenantId, String title, double value);
    Optional<MineralDustAerosolOpticalDepthNode> findMineralDustAerosolOpticalDepthNodeById(String id, String tenantId);
    MineralDustAerosolOpticalDepthNode processOptimization(String id, String tenantId);
}
