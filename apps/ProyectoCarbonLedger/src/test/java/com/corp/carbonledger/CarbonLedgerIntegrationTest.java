package com.corp.carbonledger;

import com.corp.carbonledger.domain.CarbonLedgerEntity;
import com.corp.carbonledger.application.CarbonLedgerUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CarbonLedgerIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CarbonLedgerUseCase useCase = new CarbonLedgerUseCase();
        CarbonLedgerEntity entity = new CarbonLedgerEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CarbonLedgerEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
