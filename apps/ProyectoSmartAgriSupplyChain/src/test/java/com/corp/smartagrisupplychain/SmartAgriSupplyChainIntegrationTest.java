package com.corp.smartagrisupplychain;

import com.corp.smartagrisupplychain.domain.SmartAgriSupplyChainEntity;
import com.corp.smartagrisupplychain.application.SmartAgriSupplyChainUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SmartAgriSupplyChainIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SmartAgriSupplyChainUseCase useCase = new SmartAgriSupplyChainUseCase();
        SmartAgriSupplyChainEntity entity = new SmartAgriSupplyChainEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SmartAgriSupplyChainEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
