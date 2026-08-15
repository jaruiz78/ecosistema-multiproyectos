package com.corp.presatwinscada;

import com.corp.presatwinscada.domain.PresaTwinSCADAEntity;
import com.corp.presatwinscada.application.PresaTwinSCADAUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class PresaTwinSCADAIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        PresaTwinSCADAUseCase useCase = new PresaTwinSCADAUseCase();
        PresaTwinSCADAEntity entity = new PresaTwinSCADAEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        PresaTwinSCADAEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
