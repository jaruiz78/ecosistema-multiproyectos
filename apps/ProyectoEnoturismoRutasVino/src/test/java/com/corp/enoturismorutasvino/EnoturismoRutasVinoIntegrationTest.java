package com.corp.enoturismorutasvino;

import com.corp.enoturismorutasvino.domain.EnoturismoRutasVinoEntity;
import com.corp.enoturismorutasvino.application.EnoturismoRutasVinoUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class EnoturismoRutasVinoIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        EnoturismoRutasVinoUseCase useCase = new EnoturismoRutasVinoUseCase();
        EnoturismoRutasVinoEntity entity = new EnoturismoRutasVinoEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        EnoturismoRutasVinoEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
