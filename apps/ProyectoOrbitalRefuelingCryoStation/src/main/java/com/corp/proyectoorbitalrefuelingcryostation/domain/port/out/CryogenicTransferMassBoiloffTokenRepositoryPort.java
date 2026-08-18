package com.corp.proyectoorbitalrefuelingcryostation.domain.port.out;

import com.corp.proyectoorbitalrefuelingcryostation.domain.model.CryogenicTransferMassBoiloffToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CryogenicTransferMassBoiloffTokenRepositoryPort {
    CryogenicTransferMassBoiloffToken save(CryogenicTransferMassBoiloffToken entity);
    Optional<CryogenicTransferMassBoiloffToken> findById(String id, String tenantId);
}
