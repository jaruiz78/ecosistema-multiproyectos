package com.corp.proyectohyporheicwaterpurification.domain.port.out;

import com.corp.proyectohyporheicwaterpurification.domain.model.HyporheicNitrateDenitrificationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HyporheicNitrateDenitrificationNodeRepositoryPort {
    HyporheicNitrateDenitrificationNode save(HyporheicNitrateDenitrificationNode entity);
    Optional<HyporheicNitrateDenitrificationNode> findById(String id, String tenantId);
}
