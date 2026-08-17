package com.corp.proyectotokenrwa.application;
import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SpotMarketEngineTest {
    @Test
    public void testExecuteTrade() {
        SpotMarketEngine engine = new SpotMarketEngine();
        TokenizedAsset asset = new TokenizedAsset("A1", "WATER_RIGHT", new BigDecimal("100"));
        EscrowTransactionSaga saga = new EscrowTransactionSaga("TX1", "PENDING");
        EscrowTransactionSaga result = engine.executeTrade(asset, saga);
        assertEquals("SETTLED", result.state());
    }
}
