package com.corp.generalista;

import com.corp.generalista.domain.GeneralistaEntity;
import com.corp.generalista.application.GeneralistaUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class GeneralistaIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        GeneralistaUseCase useCase = new GeneralistaUseCase();
        GeneralistaEntity entity = new GeneralistaEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        GeneralistaEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
