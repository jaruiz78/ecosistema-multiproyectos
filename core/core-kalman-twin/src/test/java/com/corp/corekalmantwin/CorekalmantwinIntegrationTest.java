package com.corp.corekalmantwin;

import com.corp.corekalmantwin.domain.CorekalmantwinEntity;
import com.corp.corekalmantwin.application.CorekalmantwinUseCase;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración Zero-Mockito con stubs in-memory.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CorekalmantwinIntegrationTest {

    @Test
    public void testDomainLogicWithoutMocks() {
        CorekalmantwinUseCase useCase = new CorekalmantwinUseCase();
        CorekalmantwinEntity entity = new CorekalmantwinEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);

        CorekalmantwinEntity result = useCase.processLogic(entity);

        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
