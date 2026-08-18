package com.corp.proyectospacedebrislasermitigation.domain.port.in;

import com.corp.proyectospacedebrislasermitigation.domain.model.SpaceDebrisConjunctionTrackToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpaceDebrisConjunctionTrackTokenUseCase {
    SpaceDebrisConjunctionTrackToken createSpaceDebrisConjunctionTrackToken(String tenantId, String title, double value);
    Optional<SpaceDebrisConjunctionTrackToken> findSpaceDebrisConjunctionTrackTokenById(String id, String tenantId);
    SpaceDebrisConjunctionTrackToken processOptimization(String id, String tenantId);
}
