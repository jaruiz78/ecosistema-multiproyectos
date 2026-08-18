package com.corp.proyectoprogrammableofflinecbdc.domain.port.out;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface OfflineCbdcSpendProofTokenRepositoryPort {
    OfflineCbdcSpendProofToken save(OfflineCbdcSpendProofToken entity);
    Optional<OfflineCbdcSpendProofToken> findById(String id, String tenantId);
}
