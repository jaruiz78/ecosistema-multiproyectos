package com.corp.coregovtechledger;

import com.corp.coregovtechledger.domain.CoregovtechledgerEntity;
import com.corp.coregovtechledger.application.CoregovtechledgerUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoregovtechledgerIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoregovtechledgerUseCase useCase = new CoregovtechledgerUseCase();
        CoregovtechledgerEntity entity = new CoregovtechledgerEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoregovtechledgerEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
