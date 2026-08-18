package com.corp.proyectohyporheicwaterpurification.domain.port.in;

import com.corp.proyectohyporheicwaterpurification.domain.model.HyporheicNitrateDenitrificationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHyporheicNitrateDenitrificationNodeUseCase {
    HyporheicNitrateDenitrificationNode createHyporheicNitrateDenitrificationNode(String tenantId, String title, double value);
    Optional<HyporheicNitrateDenitrificationNode> findHyporheicNitrateDenitrificationNodeById(String id, String tenantId);
    HyporheicNitrateDenitrificationNode processOptimization(String id, String tenantId);
}
