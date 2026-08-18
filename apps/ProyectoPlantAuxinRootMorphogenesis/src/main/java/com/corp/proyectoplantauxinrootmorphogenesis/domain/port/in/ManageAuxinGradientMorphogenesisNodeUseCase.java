package com.corp.proyectoplantauxinrootmorphogenesis.domain.port.in;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAuxinGradientMorphogenesisNodeUseCase {
    AuxinGradientMorphogenesisNode createAuxinGradientMorphogenesisNode(String tenantId, String title, double value);
    Optional<AuxinGradientMorphogenesisNode> findAuxinGradientMorphogenesisNodeById(String id, String tenantId);
    AuxinGradientMorphogenesisNode processOptimization(String id, String tenantId);
}
