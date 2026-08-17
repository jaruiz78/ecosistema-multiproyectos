package com.corp.proyectocriticalsupplyrisk.application;

import com.corp.proyectocriticalsupplyrisk.application.service.SupplyChainCascadeRiskService;
import com.corp.proyectocriticalsupplyrisk.infrastructure.adapter.out.persistence.InMemorySupplyChainRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplyChainCascadeRiskServiceTest {

    @Test
    @DisplayName("Debe evaluar y persistir nodo vulnerable ante shock logístico")
    void testEvaluateChokepointVulnerability() {
        var repo = new InMemorySupplyChainRepositoryAdapter();
        var service = new SupplyChainCascadeRiskService(repo);

        var result = service.evaluateChokepointVulnerability("CHOKE-SUEZ-01", 0.4);

        assertNotNull(result);
        assertEquals("CHOKE-SUEZ-01", result.nodeId());
        assertTrue(result.geopoliticalRiskIndex() > 0.4);
    }
}
