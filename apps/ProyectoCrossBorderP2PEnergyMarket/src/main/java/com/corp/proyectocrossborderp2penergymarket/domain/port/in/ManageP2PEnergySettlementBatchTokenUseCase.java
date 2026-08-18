package com.corp.proyectocrossborderp2penergymarket.domain.port.in;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageP2PEnergySettlementBatchTokenUseCase {
    P2PEnergySettlementBatchToken createP2PEnergySettlementBatchToken(String tenantId, String title, double value);
    Optional<P2PEnergySettlementBatchToken> findP2PEnergySettlementBatchTokenById(String id, String tenantId);
    P2PEnergySettlementBatchToken processOptimization(String id, String tenantId);
}
