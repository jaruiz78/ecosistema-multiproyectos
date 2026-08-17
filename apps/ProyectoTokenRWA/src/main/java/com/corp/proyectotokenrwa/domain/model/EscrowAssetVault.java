package com.corp.proyectotokenrwa.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: EscrowAssetVault.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record EscrowAssetVault(
    String vaultId, String underlyingAssetRef, double totalValuationEur, boolean isAudited,
    Instant timestamp
) {
    public EscrowAssetVault {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(totalValuationEur >= 0.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en EscrowAssetVault");
        }
    }
}
