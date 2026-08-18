package com.corp.proyectospacedebrislasermitigation.domain.port.out;

import com.corp.proyectospacedebrislasermitigation.domain.model.SpaceDebrisConjunctionTrackToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpaceDebrisConjunctionTrackTokenRepositoryPort {
    SpaceDebrisConjunctionTrackToken save(SpaceDebrisConjunctionTrackToken entity);
    Optional<SpaceDebrisConjunctionTrackToken> findById(String id, String tenantId);
}
