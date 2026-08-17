package com.corp.proyectocriticalsupplyrisk.domain;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplyChainNodeTest {

    @Test
    @DisplayName("Debe degradar capacidad y aumentar índice de riesgo ante disrupción severa")
    void testDisruptionSimulation() {
        SupplyChainNode node = new SupplyChainNode("NODE-MALACCA-01", "LITHIUM", 1000.0, 5000.0, 0.2, true);
        var disrupted = node.simulateDisruption(0.5); // 50% bloqueo

        assertEquals(500.0, disrupted.dailyCapacityTonnes(), 1e-3);
        assertEquals(0.45, disrupted.geopoliticalRiskIndex(), 1e-3);
    }
}
