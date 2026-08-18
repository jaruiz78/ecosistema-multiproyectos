package com.corp.proyectocrossborderp2penergymarket.application.service;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import com.corp.proyectocrossborderp2penergymarket.domain.port.in.ManageP2PEnergySettlementBatchTokenUseCase;
import com.corp.proyectocrossborderp2penergymarket.domain.port.out.P2PEnergySettlementBatchTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de P2PEnergySettlementBatchToken.
 */
@Service
public class P2PEnergySettlementBatchTokenApplicationService implements ManageP2PEnergySettlementBatchTokenUseCase {

    private final P2PEnergySettlementBatchTokenRepositoryPort repositoryPort;

    public P2PEnergySettlementBatchTokenApplicationService(P2PEnergySettlementBatchTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public P2PEnergySettlementBatchToken createP2PEnergySettlementBatchToken(String tenantId, String title, double value) {
        P2PEnergySettlementBatchToken entity = new P2PEnergySettlementBatchToken(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<P2PEnergySettlementBatchToken> findP2PEnergySettlementBatchTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public P2PEnergySettlementBatchToken processOptimization(String id, String tenantId) {
        P2PEnergySettlementBatchToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        P2PEnergySettlementBatchToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
