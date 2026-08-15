package com.corp.defensa;

import com.corp.defensa.domain.DefensaEntity;
import com.corp.defensa.application.DefensaUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class DefensaIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        DefensaUseCase useCase = new DefensaUseCase();
        DefensaEntity entity = new DefensaEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        DefensaEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
