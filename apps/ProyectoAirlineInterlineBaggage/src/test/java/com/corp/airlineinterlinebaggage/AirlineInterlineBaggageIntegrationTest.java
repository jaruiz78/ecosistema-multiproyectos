package com.corp.airlineinterlinebaggage;

import com.corp.airlineinterlinebaggage.domain.AirlineInterlineBaggageEntity;
import com.corp.airlineinterlinebaggage.application.AirlineInterlineBaggageUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AirlineInterlineBaggageIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AirlineInterlineBaggageUseCase useCase = new AirlineInterlineBaggageUseCase();
        AirlineInterlineBaggageEntity entity = new AirlineInterlineBaggageEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AirlineInterlineBaggageEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
