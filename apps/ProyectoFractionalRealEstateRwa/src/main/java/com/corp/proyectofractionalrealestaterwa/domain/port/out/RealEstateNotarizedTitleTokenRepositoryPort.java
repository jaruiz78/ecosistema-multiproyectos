package com.corp.proyectofractionalrealestaterwa.domain.port.out;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface RealEstateNotarizedTitleTokenRepositoryPort {
    RealEstateNotarizedTitleToken save(RealEstateNotarizedTitleToken entity);
    Optional<RealEstateNotarizedTitleToken> findById(String id, String tenantId);
}
