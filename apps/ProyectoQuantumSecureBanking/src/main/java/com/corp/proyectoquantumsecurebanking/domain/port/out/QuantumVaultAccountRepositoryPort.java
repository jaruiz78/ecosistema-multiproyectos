package com.corp.proyectoquantumsecurebanking.domain.port.out;

import com.corp.proyectoquantumsecurebanking.domain.model.QuantumVaultAccount;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuantumVaultAccountRepositoryPort {
    QuantumVaultAccount save(QuantumVaultAccount entity);
    Optional<QuantumVaultAccount> findById(String id, String tenantId);
}
