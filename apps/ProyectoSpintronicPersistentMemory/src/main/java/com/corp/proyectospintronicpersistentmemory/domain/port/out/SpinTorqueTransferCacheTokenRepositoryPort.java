package com.corp.proyectospintronicpersistentmemory.domain.port.out;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpinTorqueTransferCacheTokenRepositoryPort {
    SpinTorqueTransferCacheToken save(SpinTorqueTransferCacheToken entity);
    Optional<SpinTorqueTransferCacheToken> findById(String id, String tenantId);
}
