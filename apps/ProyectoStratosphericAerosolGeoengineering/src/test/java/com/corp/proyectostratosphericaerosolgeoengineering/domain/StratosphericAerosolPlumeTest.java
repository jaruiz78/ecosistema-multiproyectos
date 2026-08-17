package com.corp.proyectostratosphericaerosolgeoengineering.domain;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StratosphericAerosolPlumeTest {

    @Test
    @DisplayName("Debe calcular forzamiento radiativo negativo para inyección de aerosoles estratosféricos")
    void testNegativeRadiativeForcing() {
        StratosphericAerosolPlume plume = StratosphericAerosolPlume.create("SAI-EQUATOR-01", 20.0, 5.0); // 5 Mt SO2

        assertTrue(plume.aerosolOpticalDepthAod() > 0.0);
        assertTrue(plume.radiativeForcingWattsPerM2() < 0.0); // Enfriamiento neto
        assertEquals(StratosphericAerosolPlume.PlumeDispersionStatus.ZONAL_DISPERSION, plume.status());
    }
}
