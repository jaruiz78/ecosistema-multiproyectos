package com.corp.seamlessintermodalhub;

import com.corp.seamlessintermodalhub.domain.SeamlessIntermodalHubEntity;
import com.corp.seamlessintermodalhub.application.SeamlessIntermodalHubUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SeamlessIntermodalHubIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SeamlessIntermodalHubUseCase useCase = new SeamlessIntermodalHubUseCase();
        SeamlessIntermodalHubEntity entity = new SeamlessIntermodalHubEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SeamlessIntermodalHubEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
