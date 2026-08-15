package com.corp.corewassersteintransport;

import com.corp.corewassersteintransport.domain.CorewassersteintransportEntity;
import com.corp.corewassersteintransport.application.CorewassersteintransportUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorewassersteintransportIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorewassersteintransportUseCase useCase = new CorewassersteintransportUseCase();
        CorewassersteintransportEntity entity = new CorewassersteintransportEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorewassersteintransportEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
