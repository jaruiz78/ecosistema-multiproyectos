package com.corp.diputacionturismorural;

import com.corp.diputacionturismorural.domain.DiputacionTurismoRuralEntity;
import com.corp.diputacionturismorural.application.DiputacionTurismoRuralUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class DiputacionTurismoRuralIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        DiputacionTurismoRuralUseCase useCase = new DiputacionTurismoRuralUseCase();
        DiputacionTurismoRuralEntity entity = new DiputacionTurismoRuralEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        DiputacionTurismoRuralEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
