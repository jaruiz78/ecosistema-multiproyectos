package com.corp.proyectofractionalrealestaterwa.domain.port.in;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageRealEstateNotarizedTitleTokenUseCase {
    RealEstateNotarizedTitleToken createRealEstateNotarizedTitleToken(String tenantId, String title, double value);
    Optional<RealEstateNotarizedTitleToken> findRealEstateNotarizedTitleTokenById(String id, String tenantId);
    RealEstateNotarizedTitleToken processOptimization(String id, String tenantId);
}
