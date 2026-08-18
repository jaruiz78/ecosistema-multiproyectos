package com.corp.proyectoprogrammableofflinecbdc.domain.port.in;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageOfflineCbdcSpendProofTokenUseCase {
    OfflineCbdcSpendProofToken createOfflineCbdcSpendProofToken(String tenantId, String title, double value);
    Optional<OfflineCbdcSpendProofToken> findOfflineCbdcSpendProofTokenById(String id, String tenantId);
    OfflineCbdcSpendProofToken processOptimization(String id, String tenantId);
}
