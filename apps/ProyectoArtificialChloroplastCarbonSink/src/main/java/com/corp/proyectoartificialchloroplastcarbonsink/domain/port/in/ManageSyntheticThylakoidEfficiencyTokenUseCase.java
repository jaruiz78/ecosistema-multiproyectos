package com.corp.proyectoartificialchloroplastcarbonsink.domain.port.in;

import com.corp.proyectoartificialchloroplastcarbonsink.domain.model.SyntheticThylakoidEfficiencyToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSyntheticThylakoidEfficiencyTokenUseCase {
    SyntheticThylakoidEfficiencyToken createSyntheticThylakoidEfficiencyToken(String tenantId, String title, double value);
    Optional<SyntheticThylakoidEfficiencyToken> findSyntheticThylakoidEfficiencyTokenById(String id, String tenantId);
    SyntheticThylakoidEfficiencyToken processOptimization(String id, String tenantId);
}
