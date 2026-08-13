package com.proyecto.circular.application;

import com.proyecto.circular.domain.BioWasteBatch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WasteTraceabilityServiceTest {

    @Test
    void testCertifyBatchLcaSuccess() {
        WasteTraceabilityService service = new WasteTraceabilityService();
        BioWasteBatch batch = new BioWasteBatch("waste_001", "ORGANIC_COMPOST", 1500.0, 85.0, false);

        BioWasteBatch certified = service.certifyBatchLca(batch, 80.0);

        assertTrue(certified.certifiedCompliant());
    }
}
