package com.proyecto.airdefense;

import com.proyecto.airdefense.application.TacticalAirDefenseService;
import com.proyecto.airdefense.domain.TacticalThreatRadar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TacticalAirDefenseServiceTest {

    private TacticalAirDefenseService service;

    @BeforeEach
    void setUp() {
        service = new TacticalAirDefenseService();
    }

    @Test
    void testEvaluateHostileThreatPrioritization() {
        var friendly = new TacticalThreatRadar("CIVIL_AIRBUS_A320", "8828308281fffff", 10000.0, 240.0, 50.0, 80.0, false);
        var hostileLowStealth = new TacticalThreatRadar("UAV_STEALTH_01", "8828308283fffff", 300.0, 300.0, 0.05, 45.0, true);

        var engagements = service.evaluateThreatMesh(List.of(friendly, hostileLowStealth));

        assertNotNull(engagements);
        assertEquals(1, engagements.size(), "Solo las trazas hostiles deben generar órdenes de intercepción");
        assertEquals("UAV_STEALTH_01", engagements.get(0).targetTrackId());
        assertTrue(engagements.get(0).threatLevel() >= 4);
        assertTrue(engagements.get(0).immediateActionRequired());
    }

    @Test
    void testNoHostileTracksReturnsEmptyList() {
        var friendly = new TacticalThreatRadar("CARGO_SHIP_01", "8828308281fffff", 0.0, 15.0, 5000.0, 110.0, false);
        var engagements = service.evaluateThreatMesh(List.of(friendly));
        assertTrue(engagements.isEmpty());
    }
}
