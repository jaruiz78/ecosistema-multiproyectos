package com.corp.quantumsatellitesync;

import com.corp.quantumsatellitesync.domain.QuantumSatelliteSyncEntity;
import com.corp.quantumsatellitesync.application.QuantumSatelliteSyncUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class QuantumSatelliteSyncIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        QuantumSatelliteSyncUseCase useCase = new QuantumSatelliteSyncUseCase();
        QuantumSatelliteSyncEntity entity = new QuantumSatelliteSyncEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        QuantumSatelliteSyncEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
