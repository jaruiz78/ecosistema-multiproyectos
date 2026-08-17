package com.corp.coregame;

import com.corp.core.math.game.VcgAuctionMechanism;
import com.corp.coregame.application.MultiUnitResourceAuctionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameAuctionIntegrationTest {

    @Test
    @DisplayName("Debe ejecutar subasta VCG y calcular ingresos de asignación")
    void testRunVcgAuctionIntegration() {
        MultiUnitResourceAuctionUseCase useCase = new MultiUnitResourceAuctionUseCase();
        var bids = List.of(
                new VcgAuctionMechanism.Bid("PORT_OPERATOR_1", 200.0, 5),
                new VcgAuctionMechanism.Bid("PORT_OPERATOR_2", 150.0, 5)
        );

        var result = useCase.runVcgAuction("AUCTION-PORT-SLOTS-01", bids, 8);

        assertNotNull(result);
        assertEquals(8, result.totalUnitsAllocated());
        assertTrue(result.strategyProofVerified());
    }
}
