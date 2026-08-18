package com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.in;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDtnBundleCustodyTransferTokenUseCase {
    DtnBundleCustodyTransferToken createDtnBundleCustodyTransferToken(String tenantId, String title, double value);
    Optional<DtnBundleCustodyTransferToken> findDtnBundleCustodyTransferTokenById(String id, String tenantId);
    DtnBundleCustodyTransferToken processOptimization(String id, String tenantId);
}
