package com.corp.proyectomarinebioacousticssonar.domain.port.out;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HydrophoneAcousticPulseNodeRepositoryPort {
    HydrophoneAcousticPulseNode save(HydrophoneAcousticPulseNode entity);
    Optional<HydrophoneAcousticPulseNode> findById(String id, String tenantId);
}
