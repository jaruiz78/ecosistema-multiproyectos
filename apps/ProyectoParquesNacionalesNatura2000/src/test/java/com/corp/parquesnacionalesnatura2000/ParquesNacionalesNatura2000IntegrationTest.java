package com.corp.parquesnacionalesnatura2000;

import com.corp.parquesnacionalesnatura2000.domain.ParquesNacionalesNatura2000Entity;
import com.corp.parquesnacionalesnatura2000.application.ParquesNacionalesNatura2000UseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class ParquesNacionalesNatura2000IntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        ParquesNacionalesNatura2000UseCase useCase = new ParquesNacionalesNatura2000UseCase();
        ParquesNacionalesNatura2000Entity entity = new ParquesNacionalesNatura2000Entity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        ParquesNacionalesNatura2000Entity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
