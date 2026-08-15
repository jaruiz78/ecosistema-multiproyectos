package com.corp.ecosystem.playas.domain.port;

import com.corp.ecosystem.playas.domain.SmartBeachTwin;
import java.util.Optional;

public interface SmartBeachRepositoryPort {
    SmartBeachTwin save(SmartBeachTwin beach);
    Optional<SmartBeachTwin> findById(SmartBeachTwin.BeachId id);
}
