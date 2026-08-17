package com.corp.proyectocarbondirectaircapture.domain;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectAirCaptureFacilityTest {

    @Test
    @DisplayName("Debe registrar lote de CO2 mineralizado en basalto con 95% de eficiencia")
    void testMineralizationBatch() {
        DirectAirCaptureFacility facility = DirectAirCaptureFacility.create("DAC-PLANT-ICELAND-01", 100.0);
        var mineralized = facility.recordMineralizationBatch(50.0); // 50 toneladas

        assertEquals(47.5, mineralized.cumulativeMineralizedTonnesCo2(), 1e-3);
        assertEquals(DirectAirCaptureFacility.FacilityStatus.INJECTING_BASALT, mineralized.status());
    }
}
