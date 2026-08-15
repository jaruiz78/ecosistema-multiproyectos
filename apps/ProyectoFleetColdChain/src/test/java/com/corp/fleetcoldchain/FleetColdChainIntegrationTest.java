package com.corp.fleetcoldchain;

import com.corp.fleetcoldchain.domain.FleetColdChainEntity;
import com.corp.fleetcoldchain.application.FleetColdChainUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class FleetColdChainIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        FleetColdChainUseCase useCase = new FleetColdChainUseCase();
        FleetColdChainEntity entity = new FleetColdChainEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        FleetColdChainEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
