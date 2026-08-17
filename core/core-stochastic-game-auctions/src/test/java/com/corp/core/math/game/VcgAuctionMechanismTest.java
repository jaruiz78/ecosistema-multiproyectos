package com.corp.core.math.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VcgAuctionMechanismTest {

    @Test
    @DisplayName("Debe asignar unidades VCG de forma eficiente a los postores con mayor valoración")
    void testSolveMultiUnitVcg() {
        var bids = List.of(
                new VcgAuctionMechanism.Bid("AIRLINE_A", 100.0, 2),
                new VcgAuctionMechanism.Bid("AIRLINE_B", 80.0, 2),
                new VcgAuctionMechanism.Bid("AIRLINE_C", 50.0, 2)
        );

        var allocations = VcgAuctionMechanism.solveMultiUnitVcg(bids, 3);

        assertEquals(3, allocations.size());
        assertEquals(2, allocations.get(0).unitsAllocated()); // AIRLINE_A obtiene 2
        assertEquals(1, allocations.get(1).unitsAllocated()); // AIRLINE_B obtiene 1
        assertEquals(0, allocations.get(2).unitsAllocated()); // AIRLINE_C obtiene 0
    }

    @Test
    @DisplayName("Debe calcular equilibrio de Stackelberg donde el líder obtiene mayor beneficio que el seguidor")
    void testStackelbergEquilibrium() {
        var eq = StackelbergEquilibriumSolver.solveLinearCournot(100.0, 20.0);

        assertTrue(eq.leaderAction() > eq.followerBestResponse());
        assertTrue(eq.leaderPayoff() > eq.followerPayoff());
    }
}
