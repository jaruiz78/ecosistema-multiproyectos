package com.corp.proyectopostquantumcertificateauthority.domain.port.in;

import com.corp.proyectopostquantumcertificateauthority.domain.model.PqHybridX509CertificateToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePqHybridX509CertificateTokenUseCase {
    PqHybridX509CertificateToken createPqHybridX509CertificateToken(String tenantId, String title, double value);
    Optional<PqHybridX509CertificateToken> findPqHybridX509CertificateTokenById(String id, String tenantId);
    PqHybridX509CertificateToken processOptimization(String id, String tenantId);
}
