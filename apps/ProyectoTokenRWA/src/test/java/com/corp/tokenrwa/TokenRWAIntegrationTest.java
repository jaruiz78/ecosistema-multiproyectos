package com.corp.tokenrwa;

import com.corp.tokenrwa.domain.TokenRWAEntity;
import com.corp.tokenrwa.application.TokenRWAUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class TokenRWAIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        TokenRWAUseCase useCase = new TokenRWAUseCase();
        TokenRWAEntity entity = new TokenRWAEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        TokenRWAEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
