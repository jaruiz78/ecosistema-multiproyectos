package com.corp.proyectoagropollinatordroneswarm.domain.port.in;

import com.corp.proyectoagropollinatordroneswarm.domain.model.PollinatorSwarmDensityNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePollinatorSwarmDensityNodeUseCase {
    PollinatorSwarmDensityNode createPollinatorSwarmDensityNode(String tenantId, String title, double value);
    Optional<PollinatorSwarmDensityNode> findPollinatorSwarmDensityNodeById(String id, String tenantId);
    PollinatorSwarmDensityNode processOptimization(String id, String tenantId);
}
