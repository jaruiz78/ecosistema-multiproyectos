package com.corp.b2g;

import com.corp.b2g.domain.B2GEntity;
import com.corp.b2g.application.B2GUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class B2GIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        B2GUseCase useCase = new B2GUseCase();
        B2GEntity entity = new B2GEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        B2GEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
