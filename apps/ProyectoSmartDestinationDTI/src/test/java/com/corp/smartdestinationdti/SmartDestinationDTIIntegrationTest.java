package com.corp.smartdestinationdti;

import com.corp.smartdestinationdti.domain.SmartDestinationDTIEntity;
import com.corp.smartdestinationdti.application.SmartDestinationDTIUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SmartDestinationDTIIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SmartDestinationDTIUseCase useCase = new SmartDestinationDTIUseCase();
        SmartDestinationDTIEntity entity = new SmartDestinationDTIEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SmartDestinationDTIEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
