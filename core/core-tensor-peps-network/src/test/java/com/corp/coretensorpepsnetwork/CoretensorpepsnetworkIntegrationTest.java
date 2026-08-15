package com.corp.coretensorpepsnetwork;

import com.corp.coretensorpepsnetwork.domain.CoretensorpepsnetworkEntity;
import com.corp.coretensorpepsnetwork.application.CoretensorpepsnetworkUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoretensorpepsnetworkIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoretensorpepsnetworkUseCase useCase = new CoretensorpepsnetworkUseCase();
        CoretensorpepsnetworkEntity entity = new CoretensorpepsnetworkEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoretensorpepsnetworkEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
