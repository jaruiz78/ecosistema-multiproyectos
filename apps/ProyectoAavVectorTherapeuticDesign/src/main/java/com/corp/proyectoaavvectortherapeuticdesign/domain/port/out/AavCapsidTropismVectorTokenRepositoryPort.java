package com.corp.proyectoaavvectortherapeuticdesign.domain.port.out;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AavCapsidTropismVectorTokenRepositoryPort {
    AavCapsidTropismVectorToken save(AavCapsidTropismVectorToken entity);
    Optional<AavCapsidTropismVectorToken> findById(String id, String tenantId);
}
