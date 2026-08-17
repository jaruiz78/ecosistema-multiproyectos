package com.corp.proyectocarbondirectaircapture.domain.port.out;

import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import java.util.Optional;

public interface DacFacilityRepositoryPort {
    DirectAirCaptureFacility save(DirectAirCaptureFacility facility);
    Optional<DirectAirCaptureFacility> findById(String facilityId);
}
