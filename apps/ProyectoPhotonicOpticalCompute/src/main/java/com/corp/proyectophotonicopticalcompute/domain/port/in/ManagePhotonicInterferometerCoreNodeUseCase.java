package com.corp.proyectophotonicopticalcompute.domain.port.in;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePhotonicInterferometerCoreNodeUseCase {
    PhotonicInterferometerCoreNode createPhotonicInterferometerCoreNode(String tenantId, String title, double value);
    Optional<PhotonicInterferometerCoreNode> findPhotonicInterferometerCoreNodeById(String id, String tenantId);
    PhotonicInterferometerCoreNode processOptimization(String id, String tenantId);
}
