package com.corp.heritagedigitaltwin3d;

import com.corp.heritagedigitaltwin3d.domain.HeritageDigitalTwin3DEntity;
import com.corp.heritagedigitaltwin3d.application.HeritageDigitalTwin3DUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class HeritageDigitalTwin3DIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        HeritageDigitalTwin3DUseCase useCase = new HeritageDigitalTwin3DUseCase();
        HeritageDigitalTwin3DEntity entity = new HeritageDigitalTwin3DEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        HeritageDigitalTwin3DEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
