package com.corp.ecosystem.microgrid.domain.port;

import com.corp.ecosystem.microgrid.domain.IndustrialMicrogridNode;
import java.util.Optional;

public interface MicrogridRepositoryPort {
    IndustrialMicrogridNode save(IndustrialMicrogridNode node);
    Optional<IndustrialMicrogridNode> findById(IndustrialMicrogridNode.NodeId id);
}
