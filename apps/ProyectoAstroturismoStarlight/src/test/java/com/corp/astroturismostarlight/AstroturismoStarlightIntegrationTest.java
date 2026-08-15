package com.corp.astroturismostarlight;

import com.corp.astroturismostarlight.domain.AstroturismoStarlightEntity;
import com.corp.astroturismostarlight.application.AstroturismoStarlightUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AstroturismoStarlightIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AstroturismoStarlightUseCase useCase = new AstroturismoStarlightUseCase();
        AstroturismoStarlightEntity entity = new AstroturismoStarlightEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AstroturismoStarlightEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
