package com.corp.coregametheoryoptimizer;

import com.corp.coregametheoryoptimizer.domain.CoregametheoryoptimizerEntity;
import com.corp.coregametheoryoptimizer.application.CoregametheoryoptimizerUseCase;
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
public class CoregametheoryoptimizerIntegrationTest {

    @Test
    public void testDomainLogicWithoutMocks() {
        CoregametheoryoptimizerUseCase useCase = new CoregametheoryoptimizerUseCase();
        CoregametheoryoptimizerEntity entity = new CoregametheoryoptimizerEntity(UUID.randomUUID(), "INIT", System.currentTimeMillis(), 100.0);

        CoregametheoryoptimizerEntity result = useCase.processLogic(entity);

        assertEquals("PROCESSED", result.state());
        assertTrue(result.metricValue() > 100.0);
    }
}
