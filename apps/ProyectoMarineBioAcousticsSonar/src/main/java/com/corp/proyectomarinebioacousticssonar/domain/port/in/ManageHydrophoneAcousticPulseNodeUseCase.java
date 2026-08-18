package com.corp.proyectomarinebioacousticssonar.domain.port.in;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHydrophoneAcousticPulseNodeUseCase {
    HydrophoneAcousticPulseNode createHydrophoneAcousticPulseNode(String tenantId, String title, double value);
    Optional<HydrophoneAcousticPulseNode> findHydrophoneAcousticPulseNodeById(String id, String tenantId);
    HydrophoneAcousticPulseNode processOptimization(String id, String tenantId);
}
