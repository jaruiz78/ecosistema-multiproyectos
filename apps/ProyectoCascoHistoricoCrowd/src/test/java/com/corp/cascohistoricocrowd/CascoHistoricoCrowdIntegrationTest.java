package com.corp.cascohistoricocrowd;

import com.corp.cascohistoricocrowd.domain.CascoHistoricoCrowdEntity;
import com.corp.cascohistoricocrowd.application.CascoHistoricoCrowdUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CascoHistoricoCrowdIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CascoHistoricoCrowdUseCase useCase = new CascoHistoricoCrowdUseCase();
        CascoHistoricoCrowdEntity entity = new CascoHistoricoCrowdEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CascoHistoricoCrowdEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
