package com.corp.proyectosyntheticenzymebiofoundry.application;

import com.corp.proyectosyntheticenzymebiofoundry.application.service.PfasDegradationEnzymeDesignService;
import com.corp.proyectosyntheticenzymebiofoundry.infrastructure.adapter.out.persistence.InMemoryEnzymeRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PfasDegradationEnzymeDesignServiceTest {

    @Test
    @DisplayName("Debe diseñar desfluorinasa y persistir secuencia candidata")
    void testDesignPfasDefluorinase() {
        var repo = new InMemoryEnzymeRepositoryAdapter();
        var service = new PfasDegradationEnzymeDesignService(repo);

        var result = service.designPfasDefluorinase("PFAS-ENZ-01", "PFAS");

        assertNotNull(result);
        assertEquals("PFAS-ENZ-01", result.enzymeDesignId());
        assertEquals("PFAS", result.targetSubstrate());
    }
}
