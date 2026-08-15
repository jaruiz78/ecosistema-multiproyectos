package com.corp.clinicaltrialszk;

import com.corp.clinicaltrialszk.domain.ClinicalTrialsZKEntity;
import com.corp.clinicaltrialszk.application.ClinicalTrialsZKUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class ClinicalTrialsZKIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        ClinicalTrialsZKUseCase useCase = new ClinicalTrialsZKUseCase();
        ClinicalTrialsZKEntity entity = new ClinicalTrialsZKEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        ClinicalTrialsZKEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
