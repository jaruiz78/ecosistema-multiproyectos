package com.corp.proyectobiometricprivacysovereignauth.domain.port.in;

import com.corp.proyectobiometricprivacysovereignauth.domain.model.EncryptedBiometricVectorDistanceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEncryptedBiometricVectorDistanceNodeUseCase {
    EncryptedBiometricVectorDistanceNode createEncryptedBiometricVectorDistanceNode(String tenantId, String title, double value);
    Optional<EncryptedBiometricVectorDistanceNode> findEncryptedBiometricVectorDistanceNodeById(String id, String tenantId);
    EncryptedBiometricVectorDistanceNode processOptimization(String id, String tenantId);
}
