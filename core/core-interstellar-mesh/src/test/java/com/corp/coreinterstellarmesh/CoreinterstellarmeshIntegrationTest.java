package com.corp.coreinterstellarmesh;

import com.corp.coreinterstellarmesh.domain.CoreinterstellarmeshEntity;
import com.corp.coreinterstellarmesh.application.CoreinterstellarmeshUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoreinterstellarmeshIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoreinterstellarmeshUseCase useCase = new CoreinterstellarmeshUseCase();
        CoreinterstellarmeshEntity entity = new CoreinterstellarmeshEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoreinterstellarmeshEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
