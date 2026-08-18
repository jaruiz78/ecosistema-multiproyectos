package com.corp.proyectospintronicpersistentmemory.domain.port.in;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpinTorqueTransferCacheTokenUseCase {
    SpinTorqueTransferCacheToken createSpinTorqueTransferCacheToken(String tenantId, String title, double value);
    Optional<SpinTorqueTransferCacheToken> findSpinTorqueTransferCacheTokenById(String id, String tenantId);
    SpinTorqueTransferCacheToken processOptimization(String id, String tenantId);
}
