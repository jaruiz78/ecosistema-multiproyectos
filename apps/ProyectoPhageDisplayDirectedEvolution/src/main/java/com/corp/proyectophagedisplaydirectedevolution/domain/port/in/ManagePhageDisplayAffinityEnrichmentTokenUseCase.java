package com.corp.proyectophagedisplaydirectedevolution.domain.port.in;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePhageDisplayAffinityEnrichmentTokenUseCase {
    PhageDisplayAffinityEnrichmentToken createPhageDisplayAffinityEnrichmentToken(String tenantId, String title, double value);
    Optional<PhageDisplayAffinityEnrichmentToken> findPhageDisplayAffinityEnrichmentTokenById(String id, String tenantId);
    PhageDisplayAffinityEnrichmentToken processOptimization(String id, String tenantId);
}
