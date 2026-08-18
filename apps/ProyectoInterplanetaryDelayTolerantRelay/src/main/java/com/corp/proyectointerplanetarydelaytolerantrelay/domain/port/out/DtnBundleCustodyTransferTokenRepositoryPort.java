package com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.out;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DtnBundleCustodyTransferTokenRepositoryPort {
    DtnBundleCustodyTransferToken save(DtnBundleCustodyTransferToken entity);
    Optional<DtnBundleCustodyTransferToken> findById(String id, String tenantId);
}
