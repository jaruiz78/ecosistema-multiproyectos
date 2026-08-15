package com.corp.vpp;

import com.corp.vpp.domain.VPPEntity;
import com.corp.vpp.application.VPPUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class VPPIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        VPPUseCase useCase = new VPPUseCase();
        VPPEntity entity = new VPPEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        VPPEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
