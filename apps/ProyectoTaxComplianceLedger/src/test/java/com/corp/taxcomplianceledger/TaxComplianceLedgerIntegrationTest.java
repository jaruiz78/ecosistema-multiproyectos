package com.corp.taxcomplianceledger;

import com.corp.taxcomplianceledger.domain.TaxComplianceLedgerEntity;
import com.corp.taxcomplianceledger.application.TaxComplianceLedgerUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class TaxComplianceLedgerIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        TaxComplianceLedgerUseCase useCase = new TaxComplianceLedgerUseCase();
        TaxComplianceLedgerEntity entity = new TaxComplianceLedgerEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        TaxComplianceLedgerEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
