package com.corp.proyectoartificialchloroplastcarbonsink.domain.port.out;

import com.corp.proyectoartificialchloroplastcarbonsink.domain.model.SyntheticThylakoidEfficiencyToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SyntheticThylakoidEfficiencyTokenRepositoryPort {
    SyntheticThylakoidEfficiencyToken save(SyntheticThylakoidEfficiencyToken entity);
    Optional<SyntheticThylakoidEfficiencyToken> findById(String id, String tenantId);
}
