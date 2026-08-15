package com.corp.coresyncmesh;

import com.corp.coresyncmesh.domain.CoresyncmeshEntity;
import com.corp.coresyncmesh.application.CoresyncmeshUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoresyncmeshIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoresyncmeshUseCase useCase = new CoresyncmeshUseCase();
        CoresyncmeshEntity entity = new CoresyncmeshEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoresyncmeshEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
