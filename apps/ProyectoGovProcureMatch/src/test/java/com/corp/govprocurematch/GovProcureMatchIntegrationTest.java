package com.corp.govprocurematch;

import com.corp.govprocurematch.domain.GovProcureMatchEntity;
import com.corp.govprocurematch.application.GovProcureMatchUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class GovProcureMatchIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        GovProcureMatchUseCase useCase = new GovProcureMatchUseCase();
        GovProcureMatchEntity entity = new GovProcureMatchEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        GovProcureMatchEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
