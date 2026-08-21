package com.corp.proyectopostquantumcertificateauthority.domain.port.in;

import com.corp.proyectopostquantumcertificateauthority.domain.model.PqHybridX509CertificateToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePqHybridX509CertificateTokenUseCase {
    PqHybridX509CertificateToken createPqHybridX509CertificateToken(String tenantId, String title, double value);
    Optional<PqHybridX509CertificateToken> findPqHybridX509CertificateTokenById(String id, String tenantId);
    PqHybridX509CertificateToken processOptimization(String id, String tenantId);
}
