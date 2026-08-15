package com.corp.playasinteligentescostas;

import com.corp.playasinteligentescostas.domain.PlayasInteligentesCostasEntity;
import com.corp.playasinteligentescostas.application.PlayasInteligentesCostasUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class PlayasInteligentesCostasIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        PlayasInteligentesCostasUseCase useCase = new PlayasInteligentesCostasUseCase();
        PlayasInteligentesCostasEntity entity = new PlayasInteligentesCostasEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        PlayasInteligentesCostasEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
