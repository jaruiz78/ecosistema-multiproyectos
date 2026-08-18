package com.corp.proyectocircadianlightingsmartcity.domain.port.out;

import com.corp.proyectocircadianlightingsmartcity.domain.model.CircadianLightingMelanopicNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CircadianLightingMelanopicNodeRepositoryPort {
    CircadianLightingMelanopicNode save(CircadianLightingMelanopicNode entity);
    Optional<CircadianLightingMelanopicNode> findById(String id, String tenantId);
}
