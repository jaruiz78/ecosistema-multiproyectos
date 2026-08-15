package com.corp.syntheticbiologyfoundry;

import com.corp.syntheticbiologyfoundry.domain.SyntheticBiologyFoundryEntity;
import com.corp.syntheticbiologyfoundry.application.SyntheticBiologyFoundryUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SyntheticBiologyFoundryIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SyntheticBiologyFoundryUseCase useCase = new SyntheticBiologyFoundryUseCase();
        SyntheticBiologyFoundryEntity entity = new SyntheticBiologyFoundryEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SyntheticBiologyFoundryEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
