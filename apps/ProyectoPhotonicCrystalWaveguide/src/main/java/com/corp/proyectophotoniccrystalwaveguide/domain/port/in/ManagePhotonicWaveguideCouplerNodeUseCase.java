package com.corp.proyectophotoniccrystalwaveguide.domain.port.in;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePhotonicWaveguideCouplerNodeUseCase {
    PhotonicWaveguideCouplerNode createPhotonicWaveguideCouplerNode(String tenantId, String title, double value);
    Optional<PhotonicWaveguideCouplerNode> findPhotonicWaveguideCouplerNodeById(String id, String tenantId);
    PhotonicWaveguideCouplerNode processOptimization(String id, String tenantId);
}
