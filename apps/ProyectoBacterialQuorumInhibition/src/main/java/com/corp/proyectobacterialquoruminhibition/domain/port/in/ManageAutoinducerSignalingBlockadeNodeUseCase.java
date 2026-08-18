package com.corp.proyectobacterialquoruminhibition.domain.port.in;

import com.corp.proyectobacterialquoruminhibition.domain.model.AutoinducerSignalingBlockadeNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAutoinducerSignalingBlockadeNodeUseCase {
    AutoinducerSignalingBlockadeNode createAutoinducerSignalingBlockadeNode(String tenantId, String title, double value);
    Optional<AutoinducerSignalingBlockadeNode> findAutoinducerSignalingBlockadeNodeById(String id, String tenantId);
    AutoinducerSignalingBlockadeNode processOptimization(String id, String tenantId);
}
