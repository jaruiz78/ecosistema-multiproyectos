package com.corp.proyectodenovoproteinenzymedesign.domain.port.out;

import com.corp.proyectodenovoproteinenzymedesign.domain.model.EnzymaticBiocatalystDesignToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EnzymaticBiocatalystDesignTokenRepositoryPort {
    EnzymaticBiocatalystDesignToken save(EnzymaticBiocatalystDesignToken entity);
    Optional<EnzymaticBiocatalystDesignToken> findById(String id, String tenantId);
}
