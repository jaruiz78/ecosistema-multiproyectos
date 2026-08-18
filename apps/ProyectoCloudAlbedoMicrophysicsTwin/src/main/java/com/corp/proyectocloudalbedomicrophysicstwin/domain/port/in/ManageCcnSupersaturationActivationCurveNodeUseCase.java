package com.corp.proyectocloudalbedomicrophysicstwin.domain.port.in;

import com.corp.proyectocloudalbedomicrophysicstwin.domain.model.CcnSupersaturationActivationCurveNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCcnSupersaturationActivationCurveNodeUseCase {
    CcnSupersaturationActivationCurveNode createCcnSupersaturationActivationCurveNode(String tenantId, String title, double value);
    Optional<CcnSupersaturationActivationCurveNode> findCcnSupersaturationActivationCurveNodeById(String id, String tenantId);
    CcnSupersaturationActivationCurveNode processOptimization(String id, String tenantId);
}
