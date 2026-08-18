package com.corp.proyectodiamondnvmagnetometry.domain.port.in;

import com.corp.proyectodiamondnvmagnetometry.domain.model.DiamondNvMagnetometerNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDiamondNvMagnetometerNodeUseCase {
    DiamondNvMagnetometerNode createDiamondNvMagnetometerNode(String tenantId, String title, double value);
    Optional<DiamondNvMagnetometerNode> findDiamondNvMagnetometerNodeById(String id, String tenantId);
    DiamondNvMagnetometerNode processOptimization(String id, String tenantId);
}
