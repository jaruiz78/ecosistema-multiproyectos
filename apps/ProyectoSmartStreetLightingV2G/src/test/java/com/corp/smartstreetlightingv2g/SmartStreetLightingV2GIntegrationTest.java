package com.corp.smartstreetlightingv2g;

import com.corp.smartstreetlightingv2g.domain.SmartStreetLightingV2GEntity;
import com.corp.smartstreetlightingv2g.application.SmartStreetLightingV2GUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SmartStreetLightingV2GIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SmartStreetLightingV2GUseCase useCase = new SmartStreetLightingV2GUseCase();
        SmartStreetLightingV2GEntity entity = new SmartStreetLightingV2GEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SmartStreetLightingV2GEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
