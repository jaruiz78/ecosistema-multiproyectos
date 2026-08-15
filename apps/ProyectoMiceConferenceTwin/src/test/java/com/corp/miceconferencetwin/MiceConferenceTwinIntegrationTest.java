package com.corp.miceconferencetwin;

import com.corp.miceconferencetwin.domain.MiceConferenceTwinEntity;
import com.corp.miceconferencetwin.application.MiceConferenceTwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class MiceConferenceTwinIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        MiceConferenceTwinUseCase useCase = new MiceConferenceTwinUseCase();
        MiceConferenceTwinEntity entity = new MiceConferenceTwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        MiceConferenceTwinEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
