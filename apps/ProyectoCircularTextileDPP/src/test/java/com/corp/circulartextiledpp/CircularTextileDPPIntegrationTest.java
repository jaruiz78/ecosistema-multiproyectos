package com.corp.circulartextiledpp;

import com.corp.circulartextiledpp.domain.CircularTextileDPPEntity;
import com.corp.circulartextiledpp.application.CircularTextileDPPUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CircularTextileDPPIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CircularTextileDPPUseCase useCase = new CircularTextileDPPUseCase();
        CircularTextileDPPEntity entity = new CircularTextileDPPEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CircularTextileDPPEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
