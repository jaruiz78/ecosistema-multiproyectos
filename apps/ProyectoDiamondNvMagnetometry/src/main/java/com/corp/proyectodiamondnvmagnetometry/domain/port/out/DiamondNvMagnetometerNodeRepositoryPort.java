package com.corp.proyectodiamondnvmagnetometry.domain.port.out;

import com.corp.proyectodiamondnvmagnetometry.domain.model.DiamondNvMagnetometerNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DiamondNvMagnetometerNodeRepositoryPort {
    DiamondNvMagnetometerNode save(DiamondNvMagnetometerNode entity);
    Optional<DiamondNvMagnetometerNode> findById(String id, String tenantId);
}
