package com.corp.ecotasasoberanatax;

import com.corp.ecotasasoberanatax.domain.EcotasaSoberanaTaxEntity;
import com.corp.ecotasasoberanatax.application.EcotasaSoberanaTaxUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
public class EcotasaSoberanaTaxIntegrationTest {
    
    @Test
    public void testDomainLogicWithoutMocks() {
        EcotasaSoberanaTaxUseCase useCase = new EcotasaSoberanaTaxUseCase();
        EcotasaSoberanaTaxEntity entity = new EcotasaSoberanaTaxEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);
        
        EcotasaSoberanaTaxEntity result = useCase.processLogic(entity);
        
        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
