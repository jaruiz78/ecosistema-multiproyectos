package com.corp.proyectoglaciermelticecapmonitor.domain.port.in;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGlacierBedrockIceThicknessNodeUseCase {
    GlacierBedrockIceThicknessNode createGlacierBedrockIceThicknessNode(String tenantId, String title, double value);
    Optional<GlacierBedrockIceThicknessNode> findGlacierBedrockIceThicknessNodeById(String id, String tenantId);
    GlacierBedrockIceThicknessNode processOptimization(String id, String tenantId);
}
