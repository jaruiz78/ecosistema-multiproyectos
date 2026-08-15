package com.corp.ecosystem.porttwin;

import com.corp.ecosystem.porttwin.application.PortTwinService;
import com.corp.ecosystem.porttwin.domain.PortTerminalTwin;
import com.corp.ecosystem.porttwin.domain.port.PortTerminalRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoPortTwinAutonomous.
 */
class PortTerminalTwinTest {

    static class InMemoryPortTerminalRepository implements PortTerminalRepositoryPort {
        private final Map<PortTerminalTwin.TerminalId, PortTerminalTwin> storage = new ConcurrentHashMap<>();

        @Override
        public PortTerminalTwin save(PortTerminalTwin terminal) {
            storage.put(terminal.id(), terminal);
            return terminal;
        }

        @Override
        public Optional<PortTerminalTwin> findById(PortTerminalTwin.TerminalId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryPortTerminalRepository repository = new InMemoryPortTerminalRepository();
    private final PortTwinService service = new PortTwinService(repository);

    @Test
    @DisplayName("Debe registrar terminal portuaria y despachar buque portacontenedores")
    void shouldRegisterTerminalAndDispatchVessel() {
        PortTerminalTwin.BerthQuay berth1 = new PortTerminalTwin.BerthQuay("BERTH-01", 16.5, 400.0, 4, false);
        PortTerminalTwin.BerthQuay berth2 = new PortTerminalTwin.BerthQuay("BERTH-02", 14.0, 350.0, 3, false);

        PortTerminalTwin terminal = service.registerTerminal(
                "autoridad-portuaria-valencia",
                "ESVLC",
                List.of(berth1, berth2),
                50000
        );

        assertNotNull(terminal.id());

        // Despacho de buque Triple-E con 3,500 movimientos TEU y 15.0m de calado
        PortTerminalTwin dispatched = service.dispatchVessel(terminal.id(), "IMO9632064", 3500, 15.0);

        assertEquals("IMO9632064", dispatched.currentSchedule().vesselImoNumber());
        assertEquals("BERTH-01", dispatched.currentSchedule().assignedBerthCode());
        assertTrue(dispatched.currentSchedule().movesPerHourExpected() > 0);
        assertTrue(dispatched.currentSchedule().estimatedTurnaroundHours() > 0);
    }
}
