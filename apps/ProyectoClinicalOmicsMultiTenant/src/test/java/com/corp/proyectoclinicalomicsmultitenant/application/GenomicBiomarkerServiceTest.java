package com.corp.proyectoclinicalomicsmultitenant.application;

import com.corp.proyectoclinicalomicsmultitenant.application.service.GenomicBiomarkerService;
import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import com.corp.proyectoclinicalomicsmultitenant.infrastructure.adapter.out.persistence.InMemoryGenomicVariantRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenomicBiomarkerServiceTest {

    @Test
    @DisplayName("Debe evaluar significancia clínica y persistir variante genómica")
    void testEvaluateVariantSignificance() {
        var repo = new InMemoryGenomicVariantRepositoryAdapter();
        var service = new GenomicBiomarkerService(repo);

        var result = service.evaluateVariantSignificance("VAR-TP53-09", "HOSP-CLINIC-BCN", 0.00005);

        assertNotNull(result);
        assertEquals(GenomicVariantRecord.ClinicalSignificance.LIKELY_PATHOGENIC, result.significance());
    }
}
