package com.proyecto.salud.application;

import com.proyecto.salud.domain.BioMedicalPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColdChainLogisticsServiceTest {

    @Test
    void testRecordTelemetryColdChainIntact() {
        ColdChainLogisticsService service = new ColdChainLogisticsService();
        BioMedicalPayload vaccine = new BioMedicalPayload("vax_covid_001", "mRNA_VACCINE", 4.0, 2.0, 8.0, true);

        BioMedicalPayload updated = service.recordTelemetry(vaccine, 5.2);

        assertTrue(updated.coldChainIntact());
    }

    @Test
    void testRecordTelemetryColdChainBreach() {
        ColdChainLogisticsService service = new ColdChainLogisticsService();
        BioMedicalPayload vaccine = new BioMedicalPayload("vax_covid_002", "mRNA_VACCINE", 4.0, 2.0, 8.0, true);

        BioMedicalPayload updated = service.recordTelemetry(vaccine, 12.5);

        assertFalse(updated.coldChainIntact());
    }
}
