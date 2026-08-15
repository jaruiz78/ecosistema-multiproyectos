package com.corp.corespatialh33d;

import com.corp.corespatialh33d.domain.Corespatialh33dEntity;
import com.corp.corespatialh33d.application.Corespatialh33dUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class Corespatialh33dIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        Corespatialh33dUseCase useCase = new Corespatialh33dUseCase();
        Corespatialh33dEntity entity = new Corespatialh33dEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        Corespatialh33dEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
