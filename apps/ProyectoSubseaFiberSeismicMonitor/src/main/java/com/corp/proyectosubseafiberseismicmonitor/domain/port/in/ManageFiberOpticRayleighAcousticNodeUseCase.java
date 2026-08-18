package com.corp.proyectosubseafiberseismicmonitor.domain.port.in;

import com.corp.proyectosubseafiberseismicmonitor.domain.model.FiberOpticRayleighAcousticNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageFiberOpticRayleighAcousticNodeUseCase {
    FiberOpticRayleighAcousticNode createFiberOpticRayleighAcousticNode(String tenantId, String title, double value);
    Optional<FiberOpticRayleighAcousticNode> findFiberOpticRayleighAcousticNodeById(String id, String tenantId);
    FiberOpticRayleighAcousticNode processOptimization(String id, String tenantId);
}
