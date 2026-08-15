package com.corp.agrobiorobotics;

import com.corp.agrobiorobotics.domain.AgroBioRoboticsEntity;
import com.corp.agrobiorobotics.application.AgroBioRoboticsUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AgroBioRoboticsIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AgroBioRoboticsUseCase useCase = new AgroBioRoboticsUseCase();
        AgroBioRoboticsEntity entity = new AgroBioRoboticsEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AgroBioRoboticsEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
