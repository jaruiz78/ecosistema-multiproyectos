package com.corp.proyectotokenrwa.domain.port.in;

import com.corp.proyectotokenrwa.domain.model.TokenRWA;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public interface ManageTokenRWAUseCase {
    TokenRWA createTokenRWA(String tenantId, String title, double value);
    Optional<TokenRWA> findTokenRWAById(String id, String tenantId);
    TokenRWA processOptimization(String id, String tenantId);
}
