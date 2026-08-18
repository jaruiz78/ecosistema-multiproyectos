package com.corp.proyectobiometricprivacysovereignauth.domain.port.out;

import com.corp.proyectobiometricprivacysovereignauth.domain.model.EncryptedBiometricVectorDistanceNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EncryptedBiometricVectorDistanceNodeRepositoryPort {
    EncryptedBiometricVectorDistanceNode save(EncryptedBiometricVectorDistanceNode entity);
    Optional<EncryptedBiometricVectorDistanceNode> findById(String id, String tenantId);
}
