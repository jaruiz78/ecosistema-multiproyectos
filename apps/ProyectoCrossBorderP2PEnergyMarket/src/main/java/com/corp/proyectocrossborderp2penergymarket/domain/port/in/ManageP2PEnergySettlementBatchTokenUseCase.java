package com.corp.proyectocrossborderp2penergymarket.domain.port.in;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageP2PEnergySettlementBatchTokenUseCase {
    P2PEnergySettlementBatchToken createP2PEnergySettlementBatchToken(String tenantId, String title, double value);
    Optional<P2PEnergySettlementBatchToken> findP2PEnergySettlementBatchTokenById(String id, String tenantId);
    P2PEnergySettlementBatchToken processOptimization(String id, String tenantId);
}
