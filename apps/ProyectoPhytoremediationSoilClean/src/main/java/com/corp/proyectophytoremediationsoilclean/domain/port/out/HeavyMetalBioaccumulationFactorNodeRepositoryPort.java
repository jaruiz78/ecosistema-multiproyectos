package com.corp.proyectophytoremediationsoilclean.domain.port.out;

import com.corp.proyectophytoremediationsoilclean.domain.model.HeavyMetalBioaccumulationFactorNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HeavyMetalBioaccumulationFactorNodeRepositoryPort {
    HeavyMetalBioaccumulationFactorNode save(HeavyMetalBioaccumulationFactorNode entity);
    Optional<HeavyMetalBioaccumulationFactorNode> findById(String id, String tenantId);
}
