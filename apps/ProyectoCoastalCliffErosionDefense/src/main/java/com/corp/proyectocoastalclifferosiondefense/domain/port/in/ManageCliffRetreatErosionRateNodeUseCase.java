package com.corp.proyectocoastalclifferosiondefense.domain.port.in;

import com.corp.proyectocoastalclifferosiondefense.domain.model.CliffRetreatErosionRateNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCliffRetreatErosionRateNodeUseCase {
    CliffRetreatErosionRateNode createCliffRetreatErosionRateNode(String tenantId, String title, double value);
    Optional<CliffRetreatErosionRateNode> findCliffRetreatErosionRateNodeById(String id, String tenantId);
    CliffRetreatErosionRateNode processOptimization(String id, String tenantId);
}
