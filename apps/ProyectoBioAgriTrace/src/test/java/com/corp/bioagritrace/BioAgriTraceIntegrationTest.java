package com.corp.bioagritrace;

import com.corp.bioagritrace.domain.BioAgriTraceEntity;
import com.corp.bioagritrace.application.BioAgriTraceUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class BioAgriTraceIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        BioAgriTraceUseCase useCase = new BioAgriTraceUseCase();
        BioAgriTraceEntity entity = new BioAgriTraceEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        BioAgriTraceEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
