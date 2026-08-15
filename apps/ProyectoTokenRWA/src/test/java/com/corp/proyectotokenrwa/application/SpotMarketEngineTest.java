package com.corp.proyectotokenrwa.application;
import com.corp.proyectotokenrwa.domain.asset.TokenizedAsset;
import com.corp.proyectotokenrwa.domain.saga.EscrowTransactionSaga;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpotMarketEngineTest {
    @Test
    public void testExecuteTrade() {
        SpotMarketEngine engine = new SpotMarketEngine();
        TokenizedAsset asset = new TokenizedAsset("A1", "WATER_RIGHT", new BigDecimal("100"), true);
        EscrowTransactionSaga saga = new EscrowTransactionSaga("TX1", "PENDING");
        EscrowTransactionSaga result = engine.executeTrade(asset, saga);
        assertEquals("SETTLED", result.state());
    }
}
