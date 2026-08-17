package com.corp.proyectosmartgridstoragevpp.domain;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryStorageUnitTest {

    @Test
    @DisplayName("Debe cargar batería aumentando SoC y degradando SoH levemente")
    void testChargeBattery() {
        BatteryStorageUnit unit = BatteryStorageUnit.create("BESS-01", 1000.0, 250.0);
        var charged = unit.charge(200.0, 200.0);

        assertEquals(70.0, charged.currentSocPct(), 1e-2);
        assertTrue(charged.stateOfHealthPct() < 100.0);
        assertEquals(BatteryStorageUnit.BatteryState.CHARGING, charged.state());
    }

    @Test
    @DisplayName("Debe descargar batería disminuyendo SoC")
    void testDischargeBattery() {
        BatteryStorageUnit unit = BatteryStorageUnit.create("BESS-01", 1000.0, 250.0);
        var discharged = unit.discharge(200.0, 200.0);

        assertEquals(30.0, discharged.currentSocPct(), 1e-2);
        assertEquals(BatteryStorageUnit.BatteryState.DISCHARGING, discharged.state());
    }
}
