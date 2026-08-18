package com.corp.proyectowildfirefrontpredictor.domain.port.out;

import com.corp.proyectowildfirefrontpredictor.domain.model.WildfirePropagationFrontNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface WildfirePropagationFrontNodeRepositoryPort {
    WildfirePropagationFrontNode save(WildfirePropagationFrontNode entity);
    Optional<WildfirePropagationFrontNode> findById(String id, String tenantId);
}
