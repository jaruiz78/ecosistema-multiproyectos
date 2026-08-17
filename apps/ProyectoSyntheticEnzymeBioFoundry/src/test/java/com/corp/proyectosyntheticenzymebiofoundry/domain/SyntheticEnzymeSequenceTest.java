package com.corp.proyectosyntheticenzymebiofoundry.domain;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntheticEnzymeSequenceTest {

    @Test
    @DisplayName("Debe diseñar enzima desfluorinasa para degradación de PFAS con alta eficiencia catalítica")
    void testPfasDefluorinaseViability() {
        SyntheticEnzymeSequence enzyme = SyntheticEnzymeSequence.create("DEFLUORINASE-PFAS-V1", "PFAS");

        assertTrue(enzyme.catalyticEfficiencyKcatKm() > 1e5);
        assertTrue(enzyme.meltingTemperatureTmCelsius() > 60.0);
        assertEquals(SyntheticEnzymeSequence.EnzymeViability.HIGH_ACTIVITY_STABLE, enzyme.viability());
    }
}
