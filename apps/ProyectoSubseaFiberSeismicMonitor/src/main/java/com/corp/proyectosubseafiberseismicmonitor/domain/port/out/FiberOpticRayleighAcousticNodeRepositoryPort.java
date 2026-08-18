package com.corp.proyectosubseafiberseismicmonitor.domain.port.out;

import com.corp.proyectosubseafiberseismicmonitor.domain.model.FiberOpticRayleighAcousticNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface FiberOpticRayleighAcousticNodeRepositoryPort {
    FiberOpticRayleighAcousticNode save(FiberOpticRayleighAcousticNode entity);
    Optional<FiberOpticRayleighAcousticNode> findById(String id, String tenantId);
}
