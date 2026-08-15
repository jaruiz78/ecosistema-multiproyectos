package com.corp.catastrofes;

import com.corp.catastrofes.domain.CatastrofesEntity;
import com.corp.catastrofes.application.CatastrofesUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class CatastrofesIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        CatastrofesUseCase useCase = new CatastrofesUseCase();
        CatastrofesEntity entity = new CatastrofesEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        CatastrofesEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
