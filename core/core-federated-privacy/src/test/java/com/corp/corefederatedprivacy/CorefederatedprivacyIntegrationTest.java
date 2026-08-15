package com.corp.corefederatedprivacy;

import com.corp.corefederatedprivacy.domain.CorefederatedprivacyEntity;
import com.corp.corefederatedprivacy.application.CorefederatedprivacyUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CorefederatedprivacyIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CorefederatedprivacyUseCase useCase = new CorefederatedprivacyUseCase();
        CorefederatedprivacyEntity entity = new CorefederatedprivacyEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CorefederatedprivacyEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
