package com.corp.corealertaggregator;

import com.corp.corealertaggregator.domain.CorealertaggregatorEntity;
import com.corp.corealertaggregator.application.CorealertaggregatorUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorealertaggregatorIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorealertaggregatorUseCase useCase = new CorealertaggregatorUseCase();
        CorealertaggregatorEntity entity = new CorealertaggregatorEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorealertaggregatorEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
