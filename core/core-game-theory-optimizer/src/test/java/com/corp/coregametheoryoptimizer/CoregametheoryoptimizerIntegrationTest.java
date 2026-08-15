package com.corp.coregametheoryoptimizer;

import com.corp.coregametheoryoptimizer.domain.CoregametheoryoptimizerEntity;
import com.corp.coregametheoryoptimizer.application.CoregametheoryoptimizerUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CoregametheoryoptimizerIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CoregametheoryoptimizerUseCase useCase = new CoregametheoryoptimizerUseCase();
        CoregametheoryoptimizerEntity entity = new CoregametheoryoptimizerEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CoregametheoryoptimizerEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
