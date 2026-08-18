package com.corp.proyectoorbitalrefuelingcryostation.domain.port.in;

import com.corp.proyectoorbitalrefuelingcryostation.domain.model.CryogenicTransferMassBoiloffToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCryogenicTransferMassBoiloffTokenUseCase {
    CryogenicTransferMassBoiloffToken createCryogenicTransferMassBoiloffToken(String tenantId, String title, double value);
    Optional<CryogenicTransferMassBoiloffToken> findCryogenicTransferMassBoiloffTokenById(String id, String tenantId);
    CryogenicTransferMassBoiloffToken processOptimization(String id, String tenantId);
}
