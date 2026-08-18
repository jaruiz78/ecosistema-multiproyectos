package com.corp.proyectosuperconductinggridprotection.domain.port.out;

import com.corp.proyectosuperconductinggridprotection.domain.model.HtsFaultCurrentLimiterNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HtsFaultCurrentLimiterNodeRepositoryPort {
    HtsFaultCurrentLimiterNode save(HtsFaultCurrentLimiterNode entity);
    Optional<HtsFaultCurrentLimiterNode> findById(String id, String tenantId);
}
