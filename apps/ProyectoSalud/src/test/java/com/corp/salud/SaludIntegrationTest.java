package com.corp.salud;

import com.corp.salud.domain.SaludEntity;
import com.corp.salud.application.SaludUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SaludIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SaludUseCase useCase = new SaludUseCase();
        SaludEntity entity = new SaludEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SaludEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
