package com.corp.proyectominimalgenomechassisfoundry.domain.port.in;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEssentialGeneSetCoverageTokenUseCase {
    EssentialGeneSetCoverageToken createEssentialGeneSetCoverageToken(String tenantId, String title, double value);
    Optional<EssentialGeneSetCoverageToken> findEssentialGeneSetCoverageTokenById(String id, String tenantId);
    EssentialGeneSetCoverageToken processOptimization(String id, String tenantId);
}
