package com.corp.ecosystem.porttwin.domain.port;

import com.corp.ecosystem.porttwin.domain.PortTerminalTwin;
import java.util.Optional;

public interface PortTerminalRepositoryPort {
    PortTerminalTwin save(PortTerminalTwin terminal);
    Optional<PortTerminalTwin> findById(PortTerminalTwin.TerminalId id);
}
