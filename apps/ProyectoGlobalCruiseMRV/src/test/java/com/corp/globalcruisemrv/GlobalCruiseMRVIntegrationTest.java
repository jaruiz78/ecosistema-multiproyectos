package com.corp.globalcruisemrv;

import com.corp.globalcruisemrv.domain.GlobalCruiseMRVEntity;
import com.corp.globalcruisemrv.application.GlobalCruiseMRVUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class GlobalCruiseMRVIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        GlobalCruiseMRVUseCase useCase = new GlobalCruiseMRVUseCase();
        GlobalCruiseMRVEntity entity = new GlobalCruiseMRVEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        GlobalCruiseMRVEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
