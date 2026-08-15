package com.corp.greenhydrogendesal;

import com.corp.greenhydrogendesal.domain.GreenHydrogenDesalEntity;
import com.corp.greenhydrogendesal.application.GreenHydrogenDesalUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class GreenHydrogenDesalIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        GreenHydrogenDesalUseCase useCase = new GreenHydrogenDesalUseCase();
        GreenHydrogenDesalEntity entity = new GreenHydrogenDesalEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        GreenHydrogenDesalEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
