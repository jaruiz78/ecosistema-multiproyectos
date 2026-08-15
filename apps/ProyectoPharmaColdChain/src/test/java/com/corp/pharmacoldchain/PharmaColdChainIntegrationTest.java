package com.corp.pharmacoldchain;

import com.corp.pharmacoldchain.domain.PharmaColdChainEntity;
import com.corp.pharmacoldchain.application.PharmaColdChainUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class PharmaColdChainIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        PharmaColdChainUseCase useCase = new PharmaColdChainUseCase();
        PharmaColdChainEntity entity = new PharmaColdChainEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        PharmaColdChainEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
