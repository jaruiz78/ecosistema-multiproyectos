package com.corp.coreagentswarm;

import com.corp.coreagentswarm.domain.CoreagentswarmEntity;
import com.corp.coreagentswarm.application.CoreagentswarmUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoreagentswarmIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoreagentswarmUseCase useCase = new CoreagentswarmUseCase();
        CoreagentswarmEntity entity = new CoreagentswarmEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoreagentswarmEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
