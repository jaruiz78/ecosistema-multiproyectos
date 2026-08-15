package com.corp.core.gametheory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NashEquilibriumAuctionTest {

    @Test
    @DisplayName("Debe casar ofertas y calcular precio de compensación estable en equilibrio de Nash")
    void shouldComputeNashClearingPrice() {
        var bids = List.of(
                new NashEquilibriumAuction.Bidder("B1", 50.0, 10.0),
                new NashEquilibriumAuction.Bidder("B2", 45.0, 15.0)
        );
        var asks = List.of(
                new NashEquilibriumAuction.AskOffer("S1", 40.0, 12.0),
                new NashEquilibriumAuction.AskOffer("S2", 42.0, 13.0)
        );

        NashEquilibriumAuction auction = NashEquilibriumAuction.computeEquilibrium("AUC-001", bids, asks);

        assertNotNull(auction.clearingResult());
        assertTrue(auction.clearingResult().isNashEquilibriumStable());
        assertEquals(44.25, auction.clearingResult().clearingPriceEur(), 1e-2);
        assertEquals(25.0, auction.clearingResult().totalVolumeMatched(), 1e-2);
    }
}
