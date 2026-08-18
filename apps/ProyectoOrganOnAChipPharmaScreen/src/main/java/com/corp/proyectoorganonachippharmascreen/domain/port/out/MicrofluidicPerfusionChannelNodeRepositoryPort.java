package com.corp.proyectoorganonachippharmascreen.domain.port.out;

import com.corp.proyectoorganonachippharmascreen.domain.model.MicrofluidicPerfusionChannelNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MicrofluidicPerfusionChannelNodeRepositoryPort {
    MicrofluidicPerfusionChannelNode save(MicrofluidicPerfusionChannelNode entity);
    Optional<MicrofluidicPerfusionChannelNode> findById(String id, String tenantId);
}
