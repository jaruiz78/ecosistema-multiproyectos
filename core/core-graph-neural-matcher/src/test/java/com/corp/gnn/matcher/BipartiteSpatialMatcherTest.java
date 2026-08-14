package com.corp.gnn.matcher;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BipartiteSpatialMatcherTest {

    @Test
    void testSolveBipartiteMatching() {
        var demands = List.of(
                new BipartiteSpatialMatcher.DemandNode("RIDER_01", "8828308281fffff", 25.0),
                new BipartiteSpatialMatcher.DemandNode("RIDER_02", "8828308283fffff", 18.0),
                new BipartiteSpatialMatcher.DemandNode("RIDER_03", "8828308285fffff", 10.0)
        );

        var supplies = List.of(
                new BipartiteSpatialMatcher.SupplyNode("DRIVER_A", "8828308281fffff", 15.0),
                new BipartiteSpatialMatcher.SupplyNode("DRIVER_B", "8828308283fffff", 12.0)
        );

        var matches = BipartiteSpatialMatcher.solveBipartiteMatching(demands, supplies);

        assertNotNull(matches);
        assertEquals(2, matches.size(), "Deben emparejarse 2 de las 3 demandas con las 2 ofertas disponibles");
        assertEquals("RIDER_01", matches.get(0).demandId());
        assertTrue(matches.get(0).utilityScore() >= 0.0);
    }

    @Test
    void testEmptyMatching() {
        var matches = BipartiteSpatialMatcher.solveBipartiteMatching(List.of(), List.of());
        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }
}
