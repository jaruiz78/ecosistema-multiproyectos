package com.corp.criticalmineralsmrv;

import com.corp.criticalmineralsmrv.domain.CriticalMineralsMRVEntity;
import com.corp.criticalmineralsmrv.application.CriticalMineralsMRVUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CriticalMineralsMRVIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CriticalMineralsMRVUseCase useCase = new CriticalMineralsMRVUseCase();
        CriticalMineralsMRVEntity entity = new CriticalMineralsMRVEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CriticalMineralsMRVEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
