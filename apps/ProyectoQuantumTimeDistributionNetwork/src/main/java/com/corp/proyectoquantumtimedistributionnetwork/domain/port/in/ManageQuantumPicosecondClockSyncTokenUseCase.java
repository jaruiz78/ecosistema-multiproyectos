package com.corp.proyectoquantumtimedistributionnetwork.domain.port.in;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuantumPicosecondClockSyncTokenUseCase {
    QuantumPicosecondClockSyncToken createQuantumPicosecondClockSyncToken(String tenantId, String title, double value);
    Optional<QuantumPicosecondClockSyncToken> findQuantumPicosecondClockSyncTokenById(String id, String tenantId);
    QuantumPicosecondClockSyncToken processOptimization(String id, String tenantId);
}
