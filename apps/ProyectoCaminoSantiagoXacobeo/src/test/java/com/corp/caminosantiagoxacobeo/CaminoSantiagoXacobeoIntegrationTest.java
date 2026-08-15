package com.corp.caminosantiagoxacobeo;

import com.corp.caminosantiagoxacobeo.domain.CaminoSantiagoXacobeoEntity;
import com.corp.caminosantiagoxacobeo.application.CaminoSantiagoXacobeoUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CaminoSantiagoXacobeoIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CaminoSantiagoXacobeoUseCase useCase = new CaminoSantiagoXacobeoUseCase();
        CaminoSantiagoXacobeoEntity entity = new CaminoSantiagoXacobeoEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CaminoSantiagoXacobeoEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
