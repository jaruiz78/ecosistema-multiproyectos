package com.corp.turismotermalbalnearios;

import com.corp.turismotermalbalnearios.domain.TurismoTermalBalneariosEntity;
import com.corp.turismotermalbalnearios.application.TurismoTermalBalneariosUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class TurismoTermalBalneariosIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        TurismoTermalBalneariosUseCase useCase = new TurismoTermalBalneariosUseCase();
        TurismoTermalBalneariosEntity entity = new TurismoTermalBalneariosEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        TurismoTermalBalneariosEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
