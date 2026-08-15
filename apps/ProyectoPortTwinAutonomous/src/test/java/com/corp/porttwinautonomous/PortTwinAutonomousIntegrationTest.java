package com.corp.porttwinautonomous;

import com.corp.porttwinautonomous.domain.PortTwinAutonomousEntity;
import com.corp.porttwinautonomous.application.PortTwinAutonomousUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class PortTwinAutonomousIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        PortTwinAutonomousUseCase useCase = new PortTwinAutonomousUseCase();
        PortTwinAutonomousEntity entity = new PortTwinAutonomousEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        PortTwinAutonomousEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
