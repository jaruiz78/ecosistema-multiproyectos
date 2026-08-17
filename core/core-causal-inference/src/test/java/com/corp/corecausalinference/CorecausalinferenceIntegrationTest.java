package com.corp.corecausalinference;

import com.corp.corecausalinference.domain.CorecausalinferenceEntity;
import com.corp.corecausalinference.application.CorecausalinferenceUseCase;
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
public class CorecausalinferenceIntegrationTest {

    @Test
    public void testDomainLogicWithoutMocks() {
        CorecausalinferenceUseCase useCase = new CorecausalinferenceUseCase();
        CorecausalinferenceEntity entity = new CorecausalinferenceEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);

        CorecausalinferenceEntity result = useCase.processLogic(entity);

        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
