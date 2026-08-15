package com.corp.redparadorestwin;

import com.corp.redparadorestwin.domain.RedParadoresTwinEntity;
import com.corp.redparadorestwin.application.RedParadoresTwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class RedParadoresTwinIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        RedParadoresTwinUseCase useCase = new RedParadoresTwinUseCase();
        RedParadoresTwinEntity entity = new RedParadoresTwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        RedParadoresTwinEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
