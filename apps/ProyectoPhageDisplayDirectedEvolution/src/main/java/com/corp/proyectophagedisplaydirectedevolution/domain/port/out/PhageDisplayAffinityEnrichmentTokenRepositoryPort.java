package com.corp.proyectophagedisplaydirectedevolution.domain.port.out;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PhageDisplayAffinityEnrichmentTokenRepositoryPort {
    PhageDisplayAffinityEnrichmentToken save(PhageDisplayAffinityEnrichmentToken entity);
    Optional<PhageDisplayAffinityEnrichmentToken> findById(String id, String tenantId);
}
