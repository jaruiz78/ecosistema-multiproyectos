package com.proyecto.v2g;

import com.proyecto.v2g.application.V2GDispatchService;
import com.proyecto.v2g.domain.V2GBatteryNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class V2GDispatchServiceTest {

    private V2GDispatchService service;

    @BeforeEach
    void setUp() {
        service = new V2GDispatchService();
    }

    @Test
    void testArbitrateFleetDischargeOnPeakTariff() {
        var v1 = new V2GBatteryNode("CAR_01", "8828308281fffff", 80.0, 90.0, 50.0, 30.0);
        var v2 = new V2GBatteryNode("CAR_02", "8828308283fffff", 60.0, 25.0, 40.0, 30.0); // No disponible (< reserve)

        var results = service.arbitrateFleetDischarge(List.of(v1, v2), 0.35, 0.25);

        assertNotNull(results);
        assertEquals(1, results.size(), "Solo CAR_01 debe inyectar energía a la red");
        assertEquals("CAR_01", results.get(0).vehicleId());
        assertTrue(results.get(0).dischargedKwh() > 0.0);
        assertTrue(results.get(0).remunerationUsd() > 0.0);
    }

    @Test
    void testNoDischargeBelowTariffThreshold() {
        var v1 = new V2GBatteryNode("CAR_01", "8828308281fffff", 80.0, 90.0, 50.0, 30.0);
        var results = service.arbitrateFleetDischarge(List.of(v1), 0.15, 0.25);
        assertTrue(results.isEmpty(), "No se debe descargar la flota si la tarifa no es pico");
    }
}
