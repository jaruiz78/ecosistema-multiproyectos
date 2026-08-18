package com.corp.proyectocrosschainassetsettlement.domain.port.in;

import com.corp.proyectocrosschainassetsettlement.domain.model.HtlcAtomicSwapEscrowLockToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHtlcAtomicSwapEscrowLockTokenUseCase {
    HtlcAtomicSwapEscrowLockToken createHtlcAtomicSwapEscrowLockToken(String tenantId, String title, double value);
    Optional<HtlcAtomicSwapEscrowLockToken> findHtlcAtomicSwapEscrowLockTokenById(String id, String tenantId);
    HtlcAtomicSwapEscrowLockToken processOptimization(String id, String tenantId);
}
