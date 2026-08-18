package com.corp.proyectoplasmonicsurfacebiosensors.domain.port.out;

import com.corp.proyectoplasmonicsurfacebiosensors.domain.model.PlasmonicResonanceShiftToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlasmonicResonanceShiftTokenRepositoryPort {
    PlasmonicResonanceShiftToken save(PlasmonicResonanceShiftToken entity);
    Optional<PlasmonicResonanceShiftToken> findById(String id, String tenantId);
}
