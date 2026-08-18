package com.corp.proyectomicrosatkabandsarconstellation.domain.port.in;

import com.corp.proyectomicrosatkabandsarconstellation.domain.model.KaBandSarImageResolutionGridNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageKaBandSarImageResolutionGridNodeUseCase {
    KaBandSarImageResolutionGridNode createKaBandSarImageResolutionGridNode(String tenantId, String title, double value);
    Optional<KaBandSarImageResolutionGridNode> findKaBandSarImageResolutionGridNodeById(String id, String tenantId);
    KaBandSarImageResolutionGridNode processOptimization(String id, String tenantId);
}
