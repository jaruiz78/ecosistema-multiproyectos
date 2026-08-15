package com.corp.energia;

import com.corp.energia.domain.EnergiaEntity;
import com.corp.energia.application.EnergiaUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class EnergiaIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        EnergiaUseCase useCase = new EnergiaUseCase();
        EnergiaEntity entity = new EnergiaEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        EnergiaEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
