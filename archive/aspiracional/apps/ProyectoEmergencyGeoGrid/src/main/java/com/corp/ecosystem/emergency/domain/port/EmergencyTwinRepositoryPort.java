package com.corp.ecosystem.emergency.domain.port;

import com.corp.ecosystem.emergency.domain.EmergencyPerimeterTwin;
import java.util.Optional;

public interface EmergencyTwinRepositoryPort {
    EmergencyPerimeterTwin save(EmergencyPerimeterTwin emergency);
    Optional<EmergencyPerimeterTwin> findById(EmergencyPerimeterTwin.EmergencyId id);
}
