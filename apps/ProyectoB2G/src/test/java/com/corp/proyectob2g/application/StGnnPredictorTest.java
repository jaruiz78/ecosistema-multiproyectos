package com.corp.proyectob2g.application;
import com.corp.proyectob2g.domain.automata.CellularState;
import com.corp.proyectob2g.domain.privacy.ZeroPiiEntity;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class StGnnPredictorTest {
    @Test
    public void testPredictSaturation() {
        StGnnPredictor predictor = new StGnnPredictor();
        List<CellularState> grid = List.of(new CellularState("C1", 10));
        List<ZeroPiiEntity> entities = List.of(new ZeroPiiEntity("anon_1", "R1"), new ZeroPiiEntity("anon_2", "R1"));
        List<CellularState> result = predictor.predictSaturation(grid, entities);
        assertEquals(12, result.get(0).saturationLevel());
    }
}
