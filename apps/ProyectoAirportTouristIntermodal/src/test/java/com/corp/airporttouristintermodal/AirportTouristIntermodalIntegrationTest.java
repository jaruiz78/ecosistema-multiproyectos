package com.corp.airporttouristintermodal;

import com.corp.airporttouristintermodal.domain.AirportTouristIntermodalEntity;
import com.corp.airporttouristintermodal.application.AirportTouristIntermodalUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AirportTouristIntermodalIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AirportTouristIntermodalUseCase useCase = new AirportTouristIntermodalUseCase();
        AirportTouristIntermodalEntity entity = new AirportTouristIntermodalEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AirportTouristIntermodalEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
