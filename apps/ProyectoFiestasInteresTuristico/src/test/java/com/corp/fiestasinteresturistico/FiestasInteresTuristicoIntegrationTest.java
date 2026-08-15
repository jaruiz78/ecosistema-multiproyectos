package com.corp.fiestasinteresturistico;

import com.corp.fiestasinteresturistico.domain.FiestasInteresTuristicoEntity;
import com.corp.fiestasinteresturistico.application.FiestasInteresTuristicoUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class FiestasInteresTuristicoIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        FiestasInteresTuristicoUseCase useCase = new FiestasInteresTuristicoUseCase();
        FiestasInteresTuristicoEntity entity = new FiestasInteresTuristicoEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        FiestasInteresTuristicoEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
