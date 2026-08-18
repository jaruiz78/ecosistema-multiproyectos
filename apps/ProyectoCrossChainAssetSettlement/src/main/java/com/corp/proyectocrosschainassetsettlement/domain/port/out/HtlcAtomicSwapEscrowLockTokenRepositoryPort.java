package com.corp.proyectocrosschainassetsettlement.domain.port.out;

import com.corp.proyectocrosschainassetsettlement.domain.model.HtlcAtomicSwapEscrowLockToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HtlcAtomicSwapEscrowLockTokenRepositoryPort {
    HtlcAtomicSwapEscrowLockToken save(HtlcAtomicSwapEscrowLockToken entity);
    Optional<HtlcAtomicSwapEscrowLockToken> findById(String id, String tenantId);
}
