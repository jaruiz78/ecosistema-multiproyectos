package com.corp.proyectoquantumtimedistributionnetwork.domain.port.out;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuantumPicosecondClockSyncTokenRepositoryPort {
    QuantumPicosecondClockSyncToken save(QuantumPicosecondClockSyncToken entity);
    Optional<QuantumPicosecondClockSyncToken> findById(String id, String tenantId);
}
