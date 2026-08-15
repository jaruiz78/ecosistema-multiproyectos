package com.corp.logistica;

import com.corp.logistica.domain.LogisticaEntity;
import com.corp.logistica.application.LogisticaUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class LogisticaIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        LogisticaUseCase useCase = new LogisticaUseCase();
        LogisticaEntity entity = new LogisticaEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        LogisticaEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
