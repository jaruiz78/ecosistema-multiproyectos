package com.corp.proyectophotonicopticalcompute.domain.port.out;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PhotonicInterferometerCoreNodeRepositoryPort {
    PhotonicInterferometerCoreNode save(PhotonicInterferometerCoreNode entity);
    Optional<PhotonicInterferometerCoreNode> findById(String id, String tenantId);
}
