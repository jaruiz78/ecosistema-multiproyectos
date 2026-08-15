package com.corp.hoteltwinrevpar;

import com.corp.hoteltwinrevpar.domain.HotelTwinRevPAREntity;
import com.corp.hoteltwinrevpar.application.HotelTwinRevPARUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class HotelTwinRevPARIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        HotelTwinRevPARUseCase useCase = new HotelTwinRevPARUseCase();
        HotelTwinRevPAREntity entity = new HotelTwinRevPAREntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        HotelTwinRevPAREntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
