package com.corp.proyectoclinicalomicsmultitenant.application;

import com.corp.proyectoclinicalomicsmultitenant.application.service.PersonalizedGeneTherapyZkOrchestratorService;
import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalizedGeneTherapyZkOrchestratorServiceTest {

    @Test
    @DisplayName("Debe coordinar clasificación genómica, atestación ZK-STARK Zero-PII y activación de circuito genético")
    void testPersonalizedGeneTherapyZkSynergy() {
        var service = new PersonalizedGeneTherapyZkOrchestratorService();

        GenomicVariantRecord variant = GenomicVariantRecord.create(
                "VAR-BRCA1-001", "HOSPITAL-LA-PAZ-MADRID", "chr17", 43044295L, "A", "G"
        ).reclassify(GenomicVariantRecord.ClinicalSignificance.PATHOGENIC, 0.0005);

        var plan = service.generatePersonalizedTherapy(
                variant,
                45.0,  // Nivel de biomarcador medido
                30.0,  // Umbral mínimo elegibilidad
                60.0   // Umbral máximo elegibilidad
        );

        assertNotNull(plan);
        assertEquals("VAR-BRCA1-001", plan.variantId());
        assertEquals("HOSPITAL-LA-PAZ-MADRID", plan.tenantHospitalId());
        assertTrue(plan.patientEligible());
        assertTrue(plan.circuitActive());
        assertNotNull(plan.zkAttestationCommitment());
    }
}
