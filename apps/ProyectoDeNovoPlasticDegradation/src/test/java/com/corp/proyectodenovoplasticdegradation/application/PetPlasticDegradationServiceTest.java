package com.corp.proyectodenovoplasticdegradation.application;

import com.corp.proyectodenovoplasticdegradation.application.service.PetPlasticDegradationService;
import com.corp.proyectodenovoplasticdegradation.infrastructure.adapter.InMemoryPolymerEnzymeRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetPlasticDegradationServiceTest {

    @Test
    @DisplayName("Debe diseñar enzima de degradación de polímeros y persistir en repositorio")
    void testEngineerEnzyme() {
        var repo = new InMemoryPolymerEnzymeRepositoryAdapter();
        var service = new PetPlasticDegradationService(repo);

        var result = service.engineerEnzyme("PETASE-MUTANT-FAST-01", "POLYETHYLENE_TEREPHTHALATE", 120.0, 65.0);

        assertNotNull(result);
        assertEquals("PETASE-MUTANT-FAST-01", result.enzymeId());
        assertEquals("POLYETHYLENE_TEREPHTHALATE", result.targetPolymerType());
        assertTrue(result.degradationRateGramsPerHour() > 0.0);

        assertTrue(repo.findById("PETASE-MUTANT-FAST-01").isPresent());
    }
}
