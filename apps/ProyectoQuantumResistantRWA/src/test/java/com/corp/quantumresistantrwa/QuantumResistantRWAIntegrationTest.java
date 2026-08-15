package com.corp.quantumresistantrwa;

import com.corp.quantumresistantrwa.domain.QuantumResistantRWAEntity;
import com.corp.quantumresistantrwa.application.QuantumResistantRWAUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class QuantumResistantRWAIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        QuantumResistantRWAUseCase useCase = new QuantumResistantRWAUseCase();
        QuantumResistantRWAEntity entity = new QuantumResistantRWAEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        QuantumResistantRWAEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
