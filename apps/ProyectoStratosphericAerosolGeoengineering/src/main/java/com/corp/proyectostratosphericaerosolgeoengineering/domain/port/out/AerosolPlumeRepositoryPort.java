package com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import java.util.Optional;

public interface AerosolPlumeRepositoryPort {
    StratosphericAerosolPlume save(StratosphericAerosolPlume plume);
    Optional<StratosphericAerosolPlume> findById(String injectionId);
}
