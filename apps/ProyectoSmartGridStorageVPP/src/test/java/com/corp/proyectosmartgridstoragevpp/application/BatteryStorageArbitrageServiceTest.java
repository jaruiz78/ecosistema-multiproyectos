package com.corp.proyectosmartgridstoragevpp.application;

import com.corp.proyectosmartgridstoragevpp.application.service.BatteryStorageArbitrageService;
import com.corp.proyectosmartgridstoragevpp.infrastructure.adapter.out.persistence.InMemoryBatteryStorageRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryStorageArbitrageServiceTest {

    @Test
    @DisplayName("Debe arbitrar cargando cuando el precio es inferior al umbral")
    void testExecuteArbitrageCharge() {
        var repo = new InMemoryBatteryStorageRepositoryAdapter();
        var service = new BatteryStorageArbitrageService(repo);

        var result = service.executeIntradayArbitrage("BESS-ARB-01", 15.0, 50.0, 1.0);

        assertNotNull(result);
        assertTrue(result.currentSocPct() > 50.0);
    }
}
