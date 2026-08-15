package com.corp.soilbiocarbontwin;

import com.corp.soilbiocarbontwin.domain.SoilBioCarbonTwinEntity;
import com.corp.soilbiocarbontwin.application.SoilBioCarbonTwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SoilBioCarbonTwinIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SoilBioCarbonTwinUseCase useCase = new SoilBioCarbonTwinUseCase();
        SoilBioCarbonTwinEntity entity = new SoilBioCarbonTwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SoilBioCarbonTwinEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
