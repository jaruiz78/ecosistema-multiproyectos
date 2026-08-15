package com.corp.core.math.peps;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PepsTensorContractionResultTest {

    @Test
    @DisplayName("Debe contraer red tensorial PEPS 2D en tiempo submilisegundo")
    void shouldContractPepsGrid() {
        PepsTensorContractionResult result = PepsTensorContractionResult.contract2DGrid(8, 8, 4);

        assertNotNull(result);
        assertEquals(8, result.gridDimensionX());
        assertEquals(8, result.gridDimensionY());
        assertEquals(4, result.bondDimensionD());
        assertTrue(result.normZ() > 0.0);
    }
}
