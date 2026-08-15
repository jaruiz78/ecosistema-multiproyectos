package com.corp.corecausalinference;

import com.corp.corecausalinference.domain.CorecausalinferenceEntity;
import com.corp.corecausalinference.application.CorecausalinferenceUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorecausalinferenceIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorecausalinferenceUseCase useCase = new CorecausalinferenceUseCase();
        CorecausalinferenceEntity entity = new CorecausalinferenceEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorecausalinferenceEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
