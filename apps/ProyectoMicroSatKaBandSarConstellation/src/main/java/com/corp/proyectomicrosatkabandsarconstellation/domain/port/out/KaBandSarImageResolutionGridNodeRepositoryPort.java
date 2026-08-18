package com.corp.proyectomicrosatkabandsarconstellation.domain.port.out;

import com.corp.proyectomicrosatkabandsarconstellation.domain.model.KaBandSarImageResolutionGridNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface KaBandSarImageResolutionGridNodeRepositoryPort {
    KaBandSarImageResolutionGridNode save(KaBandSarImageResolutionGridNode entity);
    Optional<KaBandSarImageResolutionGridNode> findById(String id, String tenantId);
}
