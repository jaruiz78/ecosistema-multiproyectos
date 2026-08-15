package com.corp.droneairspaceuspace;

import com.corp.droneairspaceuspace.domain.DroneAirspaceUSpaceEntity;
import com.corp.droneairspaceuspace.application.DroneAirspaceUSpaceUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class DroneAirspaceUSpaceIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        DroneAirspaceUSpaceUseCase useCase = new DroneAirspaceUSpaceUseCase();
        DroneAirspaceUSpaceEntity entity = new DroneAirspaceUSpaceEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        DroneAirspaceUSpaceEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
