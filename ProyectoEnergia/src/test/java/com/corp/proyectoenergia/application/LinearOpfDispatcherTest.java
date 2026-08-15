package com.corp.proyectoenergia.application;
import com.corp.proyectoenergia.domain.grid.PowerNode;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinearOpfDispatcherTest {
    @Test
    public void testDispatchPower() {
        LinearOpfDispatcher dispatcher = new LinearOpfDispatcher();
        List<PowerNode> grid = List.of(new PowerNode("N1", 100.0, 50.0), new PowerNode("N2", 100.0, 50.0));
        List<PowerNode> result = dispatcher.dispatchPower(grid, 20.0);
        assertEquals(60.0, result.get(0).currentLoad(), 0.01);
        assertEquals(60.0, result.get(1).currentLoad(), 0.01);
    }
}
