package com.proyecto.desal;

import com.proyecto.desal.application.DesalinationDispatchService;
import com.proyecto.desal.domain.DesalinationPlant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DesalinationDispatchServiceTest {

    private DesalinationDispatchService service;

    @BeforeEach
    void setUp() {
        service = new DesalinationDispatchService();
    }

    @Test
    void testOptimizeDesalinationWithSolarSurplus() {
        var plant = new DesalinationPlant("DESAL_COSTA_01", "8828308281fffff", 500.0, 3.5, 20.0, 65.0);
        // maxPower = 500 * 3.5 = 1750 kW

        var schedules = service.optimizeDesalinationWithRenewables(List.of(plant), 875.0); // 50% solar surplus

        assertNotNull(schedules);
        assertEquals(1, schedules.size());
        assertEquals("DESAL_COSTA_01", schedules.get(0).plantId());
        assertEquals(50.0, schedules.get(0).targetProductionRatePercent(), 0.5);
        assertEquals(250.0, schedules.get(0).producedWaterM3(), 1.0);
    }

    @Test
    void testZeroSurplusYieldsZeroSchedule() {
        var plant = new DesalinationPlant("DESAL_COSTA_01", "8828308281fffff", 500.0, 3.5, 20.0, 65.0);
        var schedules = service.optimizeDesalinationWithRenewables(List.of(plant), 0.0);
        assertEquals(0.0, schedules.get(0).targetProductionRatePercent());
    }
}
