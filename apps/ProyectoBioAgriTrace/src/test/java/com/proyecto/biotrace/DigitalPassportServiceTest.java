package com.proyecto.biotrace;

import com.proyecto.biotrace.application.DigitalPassportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalPassportServiceTest {

    private DigitalPassportService service;

    @BeforeEach
    void setUp() {
        service = new DigitalPassportService();
    }

    @Test
    void testIssueCertifiedBioPassport() {
        var passport = service.issueDigitalPassport("HARVEST_OLIVES_2026_01", "8828308281fffff", "PICUAL_OLIVE", 95.0, 60.0, true);

        assertNotNull(passport);
        assertEquals("HARVEST_OLIVES_2026_01", passport.batchId());
        assertTrue(passport.isBioExportCertified(), "Debe cumplir certificación de exportación bio");
        assertNotNull(passport.merkleQrDigest());
        assertEquals(64, passport.merkleQrDigest().length());
    }

    @Test
    void testUncertifiedHighWaterFootprint() {
        var passport = service.issueDigitalPassport("HARVEST_TOMATO_02", "8828308283fffff", "CHERRY_TOMATO", 350.0, 80.0, true);
        assertFalse(passport.isBioExportCertified(), "No debe ser certificado si excede la huella hídrica máxima");
    }
}
