package com.corp.proyectoquantumentropyrngnetwork.domain.port.in;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQrngEntropySourceBlockTokenUseCase {
    QrngEntropySourceBlockToken createQrngEntropySourceBlockToken(String tenantId, String title, double value);
    Optional<QrngEntropySourceBlockToken> findQrngEntropySourceBlockTokenById(String id, String tenantId);
    QrngEntropySourceBlockToken processOptimization(String id, String tenantId);
}
