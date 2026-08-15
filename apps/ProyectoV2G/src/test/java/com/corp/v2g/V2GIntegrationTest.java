package com.corp.v2g;

import com.corp.v2g.domain.V2GEntity;
import com.corp.v2g.application.V2GUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class V2GIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        V2GUseCase useCase = new V2GUseCase();
        V2GEntity entity = new V2GEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        V2GEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
