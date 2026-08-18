package com.corp.proyectocloudalbedomicrophysicstwin.domain.port.out;

import com.corp.proyectocloudalbedomicrophysicstwin.domain.model.CcnSupersaturationActivationCurveNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CcnSupersaturationActivationCurveNodeRepositoryPort {
    CcnSupersaturationActivationCurveNode save(CcnSupersaturationActivationCurveNode entity);
    Optional<CcnSupersaturationActivationCurveNode> findById(String id, String tenantId);
}
