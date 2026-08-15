package com.corp.segitturdtistandard;

import com.corp.segitturdtistandard.domain.SegitturDtiStandardEntity;
import com.corp.segitturdtistandard.application.SegitturDtiStandardUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SegitturDtiStandardIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SegitturDtiStandardUseCase useCase = new SegitturDtiStandardUseCase();
        SegitturDtiStandardEntity entity = new SegitturDtiStandardEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SegitturDtiStandardEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
