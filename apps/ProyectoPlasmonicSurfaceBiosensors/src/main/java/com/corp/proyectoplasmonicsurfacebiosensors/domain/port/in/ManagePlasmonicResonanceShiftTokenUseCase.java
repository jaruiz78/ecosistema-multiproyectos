package com.corp.proyectoplasmonicsurfacebiosensors.domain.port.in;

import com.corp.proyectoplasmonicsurfacebiosensors.domain.model.PlasmonicResonanceShiftToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlasmonicResonanceShiftTokenUseCase {
    PlasmonicResonanceShiftToken createPlasmonicResonanceShiftToken(String tenantId, String title, double value);
    Optional<PlasmonicResonanceShiftToken> findPlasmonicResonanceShiftTokenById(String id, String tenantId);
    PlasmonicResonanceShiftToken processOptimization(String id, String tenantId);
}
