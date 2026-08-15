package com.corp.corepinnsolver;

import com.corp.corepinnsolver.domain.CorepinnsolverEntity;
import com.corp.corepinnsolver.application.CorepinnsolverUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorepinnsolverIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorepinnsolverUseCase useCase = new CorepinnsolverUseCase();
        CorepinnsolverEntity entity = new CorepinnsolverEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorepinnsolverEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
