package com.corp.ecosystem.porttwin.application;

import com.corp.ecosystem.porttwin.domain.PortTerminalTwin;
import com.corp.ecosystem.porttwin.domain.port.PortTerminalRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PortTwinService {

    private final PortTerminalRepositoryPort repositoryPort;

    public PortTwinService(PortTerminalRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public PortTerminalTwin registerTerminal(
            String tenantId,
            String portUnLoCode,
            List<PortTerminalTwin.BerthQuay> berths,
            int maxTeuCapacity
    ) {
        PortTerminalTwin.YardStorageState yard = new PortTerminalTwin.YardStorageState(0, maxTeuCapacity, 0.0);
        PortTerminalTwin.CraneDispatchSchedule schedule = new PortTerminalTwin.CraneDispatchSchedule("NONE", "NONE", 0, 0.0);

        PortTerminalTwin terminal = new PortTerminalTwin(
                new PortTerminalTwin.TerminalId("PORT-" + System.nanoTime()),
                tenantId,
                portUnLoCode,
                berths,
                yard,
                schedule,
                Instant.now()
        );
        return repositoryPort.save(terminal);
    }

    public PortTerminalTwin dispatchVessel(PortTerminalTwin.TerminalId id, String imoNumber, int teuMoves, double vesselDraft) {
        PortTerminalTwin terminal = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Terminal portuaria no encontrada: " + id.value()));

        PortTerminalTwin updated = terminal.scheduleVesselBerthing(imoNumber, teuMoves, vesselDraft);
        return repositoryPort.save(updated);
    }

    public Optional<PortTerminalTwin> getTerminal(PortTerminalTwin.TerminalId id) {
        return repositoryPort.findById(id);
    }
}
