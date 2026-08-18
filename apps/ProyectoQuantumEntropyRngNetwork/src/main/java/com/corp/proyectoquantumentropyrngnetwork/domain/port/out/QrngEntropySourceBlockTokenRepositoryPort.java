package com.corp.proyectoquantumentropyrngnetwork.domain.port.out;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QrngEntropySourceBlockTokenRepositoryPort {
    QrngEntropySourceBlockToken save(QrngEntropySourceBlockToken entity);
    Optional<QrngEntropySourceBlockToken> findById(String id, String tenantId);
}
