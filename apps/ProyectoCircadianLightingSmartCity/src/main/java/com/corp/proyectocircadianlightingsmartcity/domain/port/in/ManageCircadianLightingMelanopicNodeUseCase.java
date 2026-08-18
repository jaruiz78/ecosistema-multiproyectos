package com.corp.proyectocircadianlightingsmartcity.domain.port.in;

import com.corp.proyectocircadianlightingsmartcity.domain.model.CircadianLightingMelanopicNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCircadianLightingMelanopicNodeUseCase {
    CircadianLightingMelanopicNode createCircadianLightingMelanopicNode(String tenantId, String title, double value);
    Optional<CircadianLightingMelanopicNode> findCircadianLightingMelanopicNodeById(String id, String tenantId);
    CircadianLightingMelanopicNode processOptimization(String id, String tenantId);
}
