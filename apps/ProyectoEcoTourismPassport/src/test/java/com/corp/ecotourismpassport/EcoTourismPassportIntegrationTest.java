package com.corp.ecotourismpassport;

import com.corp.ecotourismpassport.domain.EcoTourismPassportEntity;
import com.corp.ecotourismpassport.application.EcoTourismPassportUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class EcoTourismPassportIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        EcoTourismPassportUseCase useCase = new EcoTourismPassportUseCase();
        EcoTourismPassportEntity entity = new EcoTourismPassportEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        EcoTourismPassportEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
