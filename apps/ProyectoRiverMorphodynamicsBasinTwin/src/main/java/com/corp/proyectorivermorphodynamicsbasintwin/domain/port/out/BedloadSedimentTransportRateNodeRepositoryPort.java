package com.corp.proyectorivermorphodynamicsbasintwin.domain.port.out;

import com.corp.proyectorivermorphodynamicsbasintwin.domain.model.BedloadSedimentTransportRateNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BedloadSedimentTransportRateNodeRepositoryPort {
    BedloadSedimentTransportRateNode save(BedloadSedimentTransportRateNode entity);
    Optional<BedloadSedimentTransportRateNode> findById(String id, String tenantId);
}
