package com.corp.coreairagengine;

import com.corp.coreairagengine.domain.CoreairagengineEntity;
import com.corp.coreairagengine.application.CoreairagengineUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoreairagengineIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoreairagengineUseCase useCase = new CoreairagengineUseCase();
        CoreairagengineEntity entity = new CoreairagengineEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoreairagengineEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
