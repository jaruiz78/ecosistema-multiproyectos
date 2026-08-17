package com.corp.corealertaggregator;

import com.corp.corealertaggregator.domain.CorealertaggregatorEntity;
import com.corp.corealertaggregator.application.CorealertaggregatorUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CorealertaggregatorIntegrationTest {

    @Test
    public void testDomainLogicWithoutMocks() {
        CorealertaggregatorUseCase useCase = new CorealertaggregatorUseCase();
        CorealertaggregatorEntity entity = new CorealertaggregatorEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);

        CorealertaggregatorEntity result = useCase.processLogic(entity);

        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
