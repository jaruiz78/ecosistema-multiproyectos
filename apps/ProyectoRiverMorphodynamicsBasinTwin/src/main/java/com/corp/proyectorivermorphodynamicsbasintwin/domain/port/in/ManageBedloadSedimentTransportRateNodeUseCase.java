package com.corp.proyectorivermorphodynamicsbasintwin.domain.port.in;

import com.corp.proyectorivermorphodynamicsbasintwin.domain.model.BedloadSedimentTransportRateNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBedloadSedimentTransportRateNodeUseCase {
    BedloadSedimentTransportRateNode createBedloadSedimentTransportRateNode(String tenantId, String title, double value);
    Optional<BedloadSedimentTransportRateNode> findBedloadSedimentTransportRateNodeById(String id, String tenantId);
    BedloadSedimentTransportRateNode processOptimization(String id, String tenantId);
}
