package com.corp.proyectoglaciermelticecapmonitor.domain.port.out;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GlacierBedrockIceThicknessNodeRepositoryPort {
    GlacierBedrockIceThicknessNode save(GlacierBedrockIceThicknessNode entity);
    Optional<GlacierBedrockIceThicknessNode> findById(String id, String tenantId);
}
