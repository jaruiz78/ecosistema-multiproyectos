package com.corp.emergencygeogrid;

import com.corp.emergencygeogrid.domain.EmergencyGeoGridEntity;
import com.corp.emergencygeogrid.application.EmergencyGeoGridUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class EmergencyGeoGridIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        EmergencyGeoGridUseCase useCase = new EmergencyGeoGridUseCase();
        EmergencyGeoGridEntity entity = new EmergencyGeoGridEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        EmergencyGeoGridEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
