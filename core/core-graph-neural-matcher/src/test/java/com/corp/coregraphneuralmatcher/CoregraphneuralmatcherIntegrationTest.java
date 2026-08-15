package com.corp.coregraphneuralmatcher;

import com.corp.coregraphneuralmatcher.domain.CoregraphneuralmatcherEntity;
import com.corp.coregraphneuralmatcher.application.CoregraphneuralmatcherUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoregraphneuralmatcherIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoregraphneuralmatcherUseCase useCase = new CoregraphneuralmatcherUseCase();
        CoregraphneuralmatcherEntity entity = new CoregraphneuralmatcherEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoregraphneuralmatcherEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
