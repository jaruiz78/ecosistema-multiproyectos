package com.corp.proyectopiezoelectrickineticharvester.domain.port.in;

import com.corp.proyectopiezoelectrickineticharvester.domain.model.PiezoelectricCantileverBeamNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePiezoelectricCantileverBeamNodeUseCase {
    PiezoelectricCantileverBeamNode createPiezoelectricCantileverBeamNode(String tenantId, String title, double value);
    Optional<PiezoelectricCantileverBeamNode> findPiezoelectricCantileverBeamNodeById(String id, String tenantId);
    PiezoelectricCantileverBeamNode processOptimization(String id, String tenantId);
}
