package com.corp.proyectogroundwater3daquifermodel.domain.port.in;

import com.corp.proyectogroundwater3daquifermodel.domain.model.AquiferHydraulicHeadPiezometerNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAquiferHydraulicHeadPiezometerNodeUseCase {
    AquiferHydraulicHeadPiezometerNode createAquiferHydraulicHeadPiezometerNode(String tenantId, String title, double value);
    Optional<AquiferHydraulicHeadPiezometerNode> findAquiferHydraulicHeadPiezometerNodeById(String id, String tenantId);
    AquiferHydraulicHeadPiezometerNode processOptimization(String id, String tenantId);
}
