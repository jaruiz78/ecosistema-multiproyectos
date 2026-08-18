package com.corp.proyectodenovoproteinenzymedesign.domain.port.in;

import com.corp.proyectodenovoproteinenzymedesign.domain.model.EnzymaticBiocatalystDesignToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEnzymaticBiocatalystDesignTokenUseCase {
    EnzymaticBiocatalystDesignToken createEnzymaticBiocatalystDesignToken(String tenantId, String title, double value);
    Optional<EnzymaticBiocatalystDesignToken> findEnzymaticBiocatalystDesignTokenById(String id, String tenantId);
    EnzymaticBiocatalystDesignToken processOptimization(String id, String tenantId);
}
