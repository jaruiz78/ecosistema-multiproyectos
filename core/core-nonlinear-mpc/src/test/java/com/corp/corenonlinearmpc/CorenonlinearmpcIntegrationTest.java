package com.corp.corenonlinearmpc;

import com.corp.corenonlinearmpc.domain.CorenonlinearmpcEntity;
import com.corp.corenonlinearmpc.application.CorenonlinearmpcUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorenonlinearmpcIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorenonlinearmpcUseCase useCase = new CorenonlinearmpcUseCase();
        CorenonlinearmpcEntity entity = new CorenonlinearmpcEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorenonlinearmpcEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
