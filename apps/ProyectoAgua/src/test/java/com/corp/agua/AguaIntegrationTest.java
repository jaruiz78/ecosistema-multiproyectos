package com.corp.agua;

import com.corp.agua.domain.AguaEntity;
import com.corp.agua.application.AguaUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AguaIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AguaUseCase useCase = new AguaUseCase();
        AguaEntity entity = new AguaEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AguaEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
