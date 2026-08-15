package com.corp.dualairdefense;

import com.corp.dualairdefense.domain.DualAirDefenseEntity;
import com.corp.dualairdefense.application.DualAirDefenseUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class DualAirDefenseIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        DualAirDefenseUseCase useCase = new DualAirDefenseUseCase();
        DualAirDefenseEntity entity = new DualAirDefenseEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        DualAirDefenseEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
