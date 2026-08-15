package com.corp.rutassenderismogr;

import com.corp.rutassenderismogr.domain.RutasSenderismoGREntity;
import com.corp.rutassenderismogr.application.RutasSenderismoGRUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class RutasSenderismoGRIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        RutasSenderismoGRUseCase useCase = new RutasSenderismoGRUseCase();
        RutasSenderismoGREntity entity = new RutasSenderismoGREntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        RutasSenderismoGREntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
