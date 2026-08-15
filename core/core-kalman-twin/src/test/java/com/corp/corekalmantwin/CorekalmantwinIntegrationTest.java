package com.corp.corekalmantwin;

import com.corp.corekalmantwin.domain.CorekalmantwinEntity;
import com.corp.corekalmantwin.application.CorekalmantwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorekalmantwinIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorekalmantwinUseCase useCase = new CorekalmantwinUseCase();
        CorekalmantwinEntity entity = new CorekalmantwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorekalmantwinEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
