package com.corp.industrialmicrogridmpc;

import com.corp.industrialmicrogridmpc.domain.IndustrialMicrogridMPCEntity;
import com.corp.industrialmicrogridmpc.application.IndustrialMicrogridMPCUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class IndustrialMicrogridMPCIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        IndustrialMicrogridMPCUseCase useCase = new IndustrialMicrogridMPCUseCase();
        IndustrialMicrogridMPCEntity entity = new IndustrialMicrogridMPCEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        IndustrialMicrogridMPCEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
