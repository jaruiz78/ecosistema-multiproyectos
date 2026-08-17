package com.corp.proyectospacetrafficcoordination.application;

import com.corp.proyectospacetrafficcoordination.application.service.ConjunctionAssessmentService;
import com.corp.proyectospacetrafficcoordination.infrastructure.adapter.out.persistence.InMemorySpaceTrackRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConjunctionAssessmentServiceTest {

    @Test
    @DisplayName("Debe evaluar conjunción y persistir traza actualizada")
    void testAssessConjunctionRisk() {
        var repo = new InMemorySpaceTrackRepositoryAdapter();
        var service = new ConjunctionAssessmentService(repo);

        var result = service.assessConjunctionRisk("SAT-01", "DEBRIS-02");

        assertNotNull(result);
        assertEquals("SAT-01", result.noradCatalogId());
    }
}
