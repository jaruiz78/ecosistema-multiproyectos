package com.corp.proyectotokenrwa.application;

import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SpotMarketEngine {
    public EscrowTransactionSaga executeTrade(TokenizedAsset asset, EscrowTransactionSaga saga) {
        if (asset.value().compareTo(java.math.BigDecimal.ZERO) > 0) {
            return new EscrowTransactionSaga(saga.transactionId(), "SETTLED");
        }
        return saga;
    }
}
