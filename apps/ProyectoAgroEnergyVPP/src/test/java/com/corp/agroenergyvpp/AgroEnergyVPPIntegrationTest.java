package com.corp.agroenergyvpp;

import com.corp.agroenergyvpp.domain.AgroEnergyVPPEntity;
import com.corp.agroenergyvpp.application.AgroEnergyVPPUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class AgroEnergyVPPIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        AgroEnergyVPPUseCase useCase = new AgroEnergyVPPUseCase();
        AgroEnergyVPPEntity entity = new AgroEnergyVPPEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        AgroEnergyVPPEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
