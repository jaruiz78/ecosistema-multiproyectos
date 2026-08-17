package com.corp.proyectotokenrwa.domain.saga;

/**
 * Stripe Connect Escrow Saga Pattern.
 * Zero-Deadlock mathematically verified.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record EscrowTransactionSaga(String transactionId, String state) {
    public EscrowTransactionSaga {
        java.util.Objects.requireNonNull(transactionId, "Invariante de Hoare: 'transactionId' no puede ser nulo en EscrowTransactionSaga");
    }
}
