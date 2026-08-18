package com.corp.proyectoorganonachippharmascreen.domain.port.in;

import com.corp.proyectoorganonachippharmascreen.domain.model.MicrofluidicPerfusionChannelNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMicrofluidicPerfusionChannelNodeUseCase {
    MicrofluidicPerfusionChannelNode createMicrofluidicPerfusionChannelNode(String tenantId, String title, double value);
    Optional<MicrofluidicPerfusionChannelNode> findMicrofluidicPerfusionChannelNodeById(String id, String tenantId);
    MicrofluidicPerfusionChannelNode processOptimization(String id, String tenantId);
}
