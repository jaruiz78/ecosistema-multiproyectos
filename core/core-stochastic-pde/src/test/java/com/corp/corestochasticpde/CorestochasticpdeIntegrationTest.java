package com.corp.corestochasticpde;

import com.corp.corestochasticpde.domain.CorestochasticpdeEntity;
import com.corp.corestochasticpde.application.CorestochasticpdeUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorestochasticpdeIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorestochasticpdeUseCase useCase = new CorestochasticpdeUseCase();
        CorestochasticpdeEntity entity = new CorestochasticpdeEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorestochasticpdeEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
