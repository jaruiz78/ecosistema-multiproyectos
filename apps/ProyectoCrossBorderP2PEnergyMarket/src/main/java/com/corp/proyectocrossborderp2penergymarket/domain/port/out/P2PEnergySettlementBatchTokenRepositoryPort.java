package com.corp.proyectocrossborderp2penergymarket.domain.port.out;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface P2PEnergySettlementBatchTokenRepositoryPort {
    P2PEnergySettlementBatchToken save(P2PEnergySettlementBatchToken entity);
    Optional<P2PEnergySettlementBatchToken> findById(String id, String tenantId);
}
