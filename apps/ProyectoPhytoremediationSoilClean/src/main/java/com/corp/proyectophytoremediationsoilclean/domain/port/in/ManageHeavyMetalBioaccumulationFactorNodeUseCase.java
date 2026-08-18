package com.corp.proyectophytoremediationsoilclean.domain.port.in;

import com.corp.proyectophytoremediationsoilclean.domain.model.HeavyMetalBioaccumulationFactorNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHeavyMetalBioaccumulationFactorNodeUseCase {
    HeavyMetalBioaccumulationFactorNode createHeavyMetalBioaccumulationFactorNode(String tenantId, String title, double value);
    Optional<HeavyMetalBioaccumulationFactorNode> findHeavyMetalBioaccumulationFactorNodeById(String id, String tenantId);
    HeavyMetalBioaccumulationFactorNode processOptimization(String id, String tenantId);
}
