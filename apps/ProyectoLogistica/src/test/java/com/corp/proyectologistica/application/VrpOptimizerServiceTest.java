package com.corp.proyectologistica.application;
import com.corp.proyectologistica.domain.vrp.RouteRecord;
import com.corp.proyectologistica.domain.spatial.H3GeoIndex;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class VrpOptimizerServiceTest {
    @Test
    public void testOptimizeRoutes() {
        VrpOptimizerService service = new VrpOptimizerService();
        H3GeoIndex index = new H3GeoIndex("8a2a1072b59ffff", 1.5);
        List<RouteRecord> routes = List.of(new RouteRecord("R1", "000", 10.0, false), new RouteRecord("R2", "000", 20.0, false));
        List<RouteRecord> optimized = service.optimizeRoutes(routes, index);
        assertEquals(2, optimized.size());
        assertEquals("R2", optimized.get(0).routeId());
        assertEquals(30.0, optimized.get(0).priorityScore(), 0.01);
    }
}
