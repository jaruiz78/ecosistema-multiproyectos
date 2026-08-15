package com.corp.proyectob2g.application;
import com.corp.proyectob2g.domain.automata.CellularState;
import com.corp.proyectob2g.domain.privacy.ZeroPiiEntity;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Arquitectura y especificación formal para StGnnPredictorTest.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
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
