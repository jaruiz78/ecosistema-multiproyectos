package com.corp.proyectotokenrwa.domain.asset;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record TokenizedAsset(String assetId, String type, java.math.BigDecimal value) {
    public TokenizedAsset {
        java.util.Objects.requireNonNull(assetId, "Invariante de Hoare: 'assetId' no puede ser nulo en TokenizedAsset");
    }
}
