package com.corp.proyectobiodiversitygenomicbank.domain;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentalDnaSampleTest {

    @Test
    @DisplayName("Debe calcular índice de Shannon-Wiener para muestra metagenómica diversa")
    void testShannonDiversityCalculation() {
        var reads = Map.of(
                "Quercus_ilex", 50,
                "Pinus_halepensis", 45,
                "Rosmarinus_officinalis", 35,
                "Thymus_vulgaris", 30,
                "Lavandula_angustifolia", 25
        );

        EnvironmentalDnaSample sample = EnvironmentalDnaSample.create("EDNA-SIERRA-NEVADA-01", "MEDITERRANEAN_FOREST", 0x881f1d4887fffffL, reads);

        assertTrue(sample.shannonDiversityIndexH() > 1.4);
        assertNotNull(sample.status());
    }
}
