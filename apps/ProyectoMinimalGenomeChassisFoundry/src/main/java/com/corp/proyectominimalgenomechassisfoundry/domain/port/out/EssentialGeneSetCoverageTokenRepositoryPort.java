package com.corp.proyectominimalgenomechassisfoundry.domain.port.out;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EssentialGeneSetCoverageTokenRepositoryPort {
    EssentialGeneSetCoverageToken save(EssentialGeneSetCoverageToken entity);
    Optional<EssentialGeneSetCoverageToken> findById(String id, String tenantId);
}
