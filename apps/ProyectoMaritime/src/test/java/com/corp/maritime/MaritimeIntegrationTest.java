package com.corp.maritime;

import com.corp.maritime.domain.MaritimeEntity;
import com.corp.maritime.application.MaritimeUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class MaritimeIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        MaritimeUseCase useCase = new MaritimeUseCase();
        MaritimeEntity entity = new MaritimeEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        MaritimeEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
