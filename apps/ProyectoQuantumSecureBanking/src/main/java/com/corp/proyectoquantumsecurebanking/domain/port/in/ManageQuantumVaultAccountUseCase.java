package com.corp.proyectoquantumsecurebanking.domain.port.in;

import com.corp.proyectoquantumsecurebanking.domain.model.QuantumVaultAccount;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuantumVaultAccountUseCase {
    QuantumVaultAccount createQuantumVaultAccount(String tenantId, String title, double value);
    Optional<QuantumVaultAccount> findQuantumVaultAccountById(String id, String tenantId);
    QuantumVaultAccount processOptimization(String id, String tenantId);
}
