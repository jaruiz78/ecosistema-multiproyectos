package com.corp.proyectodefensa.domain.port.in;

import com.corp.proyectodefensa.domain.model.Defensa;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_10_identidad_soberana_privacidad_zkp">FACULTAD_XI: Identidad Soberana & Zero-Trust BeyondCorp</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageDefensaUseCase {
    Defensa createDefensa(String tenantId, String title, double value);
    Optional<Defensa> findDefensaById(String id, String tenantId);
    Defensa processOptimization(String id, String tenantId);
}
