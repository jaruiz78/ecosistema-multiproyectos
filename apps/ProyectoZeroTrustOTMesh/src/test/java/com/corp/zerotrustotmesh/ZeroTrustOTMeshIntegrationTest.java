package com.corp.zerotrustotmesh;

import com.corp.zerotrustotmesh.domain.ZeroTrustOTMeshEntity;
import com.corp.zerotrustotmesh.application.ZeroTrustOTMeshUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class ZeroTrustOTMeshIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        ZeroTrustOTMeshUseCase useCase = new ZeroTrustOTMeshUseCase();
        ZeroTrustOTMeshEntity entity = new ZeroTrustOTMeshEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        ZeroTrustOTMeshEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
