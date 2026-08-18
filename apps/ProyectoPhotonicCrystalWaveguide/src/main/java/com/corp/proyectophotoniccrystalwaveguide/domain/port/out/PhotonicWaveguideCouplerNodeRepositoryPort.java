package com.corp.proyectophotoniccrystalwaveguide.domain.port.out;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PhotonicWaveguideCouplerNodeRepositoryPort {
    PhotonicWaveguideCouplerNode save(PhotonicWaveguideCouplerNode entity);
    Optional<PhotonicWaveguideCouplerNode> findById(String id, String tenantId);
}
