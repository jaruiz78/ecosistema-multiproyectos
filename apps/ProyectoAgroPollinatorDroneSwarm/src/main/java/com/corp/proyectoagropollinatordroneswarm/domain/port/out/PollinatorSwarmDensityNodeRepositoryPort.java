package com.corp.proyectoagropollinatordroneswarm.domain.port.out;

import com.corp.proyectoagropollinatordroneswarm.domain.model.PollinatorSwarmDensityNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PollinatorSwarmDensityNodeRepositoryPort {
    PollinatorSwarmDensityNode save(PollinatorSwarmDensityNode entity);
    Optional<PollinatorSwarmDensityNode> findById(String id, String tenantId);
}
