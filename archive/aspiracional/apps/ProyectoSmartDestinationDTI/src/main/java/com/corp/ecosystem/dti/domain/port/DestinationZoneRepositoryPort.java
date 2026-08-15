package com.corp.ecosystem.dti.domain.port;

import com.corp.ecosystem.dti.domain.SmartDestinationZone;
import java.util.Optional;

public interface DestinationZoneRepositoryPort {
    SmartDestinationZone save(SmartDestinationZone zone);
    Optional<SmartDestinationZone> findById(SmartDestinationZone.ZoneId id);
}
