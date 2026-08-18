package com.corp.proyectobacterialquoruminhibition.domain.port.out;

import com.corp.proyectobacterialquoruminhibition.domain.model.AutoinducerSignalingBlockadeNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AutoinducerSignalingBlockadeNodeRepositoryPort {
    AutoinducerSignalingBlockadeNode save(AutoinducerSignalingBlockadeNode entity);
    Optional<AutoinducerSignalingBlockadeNode> findById(String id, String tenantId);
}
