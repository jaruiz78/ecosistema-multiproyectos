package com.corp.subsurfacegeotwin;

import com.corp.subsurfacegeotwin.domain.SubSurfaceGeoTwinEntity;
import com.corp.subsurfacegeotwin.application.SubSurfaceGeoTwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class SubSurfaceGeoTwinIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        SubSurfaceGeoTwinUseCase useCase = new SubSurfaceGeoTwinUseCase();
        SubSurfaceGeoTwinEntity entity = new SubSurfaceGeoTwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        SubSurfaceGeoTwinEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
