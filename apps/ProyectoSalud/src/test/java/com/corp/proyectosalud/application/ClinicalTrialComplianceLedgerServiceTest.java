package com.corp.proyectosalud.application;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas TDD Zero-Mockito para {@link ClinicalTrialComplianceLedgerService}.
 */
class ClinicalTrialComplianceLedgerServiceTest {

    @Test
    @DisplayName("Debe sellar la custodia manteniendo cadena de frío y anonimización Zero-PII")
    void shouldSealCustodyWithColdChainMaintained() {
        ClinicalTrialComplianceLedgerService service = new ClinicalTrialComplianceLedgerService();

        ClinicalTrialSample sample = new ClinicalTrialSample(
                UUID.randomUUID().toString(),
                "TENANT_HOSPITAL_01",
                "PLASMA_SAMPLE_A",
                1.0,
                "STORED",
                Instant.now()
        );

        // Muestra en ultra-congelación: -75°C en rango [-80°C, -70°C]
        var seal = service.sealSampleCustody(sample, -75.0, -80.0, -70.0, "PATIENT_SSN_12345678");

        assertTrue(seal.coldChainMaintained());
        assertNotNull(seal.zeroPiiSubjectDigest());
        assertNotEquals("PATIENT_SSN_12345678", seal.zeroPiiSubjectDigest());
        assertNotNull(seal.custodySealHash());
    }

    @Test
    @DisplayName("Debe registrar fallo de cadena de frío cuando la temperatura supera el límite")
    void shouldDetectColdChainBreach() {
        ClinicalTrialComplianceLedgerService service = new ClinicalTrialComplianceLedgerService();

        ClinicalTrialSample sample = new ClinicalTrialSample(
                UUID.randomUUID().toString(),
                "TENANT_HOSPITAL_02",
                "VACCINE_VIAL_B",
                1.0,
                "TRANSIT",
                Instant.now()
        );

        // Temperatura excedida: 12°C en rango [2°C, 8°C]
        var seal = service.sealSampleCustody(sample, 12.0, 2.0, 8.0, "PATIENT_999");

        assertFalse(seal.coldChainMaintained());
    }
}
