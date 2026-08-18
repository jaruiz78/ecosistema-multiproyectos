package com.corp.proyectowildfirefrontpredictor.domain.port.in;

import com.corp.proyectowildfirefrontpredictor.domain.model.WildfirePropagationFrontNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageWildfirePropagationFrontNodeUseCase {
    WildfirePropagationFrontNode createWildfirePropagationFrontNode(String tenantId, String title, double value);
    Optional<WildfirePropagationFrontNode> findWildfirePropagationFrontNodeById(String id, String tenantId);
    WildfirePropagationFrontNode processOptimization(String id, String tenantId);
}
