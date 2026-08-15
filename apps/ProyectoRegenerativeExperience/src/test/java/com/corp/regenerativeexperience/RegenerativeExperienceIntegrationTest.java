package com.corp.regenerativeexperience;

import com.corp.regenerativeexperience.domain.RegenerativeExperienceEntity;
import com.corp.regenerativeexperience.application.RegenerativeExperienceUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class RegenerativeExperienceIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        RegenerativeExperienceUseCase useCase = new RegenerativeExperienceUseCase();
        RegenerativeExperienceEntity entity = new RegenerativeExperienceEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        RegenerativeExperienceEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
