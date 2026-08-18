package com.corp.proyectopostquantumcertificateauthority.domain.port.out;

import com.corp.proyectopostquantumcertificateauthority.domain.model.PqHybridX509CertificateToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PqHybridX509CertificateTokenRepositoryPort {
    PqHybridX509CertificateToken save(PqHybridX509CertificateToken entity);
    Optional<PqHybridX509CertificateToken> findById(String id, String tenantId);
}
