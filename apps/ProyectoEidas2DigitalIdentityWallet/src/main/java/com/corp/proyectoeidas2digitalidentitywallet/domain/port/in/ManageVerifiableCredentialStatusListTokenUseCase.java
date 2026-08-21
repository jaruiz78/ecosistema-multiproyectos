package com.corp.proyectoeidas2digitalidentitywallet.domain.port.in;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageVerifiableCredentialStatusListTokenUseCase {
    VerifiableCredentialStatusListToken createVerifiableCredentialStatusListToken(String tenantId, String title, double value);
    Optional<VerifiableCredentialStatusListToken> findVerifiableCredentialStatusListTokenById(String id, String tenantId);
    VerifiableCredentialStatusListToken processOptimization(String id, String tenantId);
}
