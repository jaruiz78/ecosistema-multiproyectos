package com.corp.proyectoplantauxinrootmorphogenesis.domain.port.out;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AuxinGradientMorphogenesisNodeRepositoryPort {
    AuxinGradientMorphogenesisNode save(AuxinGradientMorphogenesisNode entity);
    Optional<AuxinGradientMorphogenesisNode> findById(String id, String tenantId);
}
