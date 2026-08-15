package com.corp.ecosystem.natura2000.domain.port;

import com.corp.ecosystem.natura2000.domain.NationalParkEcoZoneTwin;
import java.util.Optional;

public interface NationalParkZoneRepositoryPort {
    NationalParkEcoZoneTwin save(NationalParkEcoZoneTwin zone);
    Optional<NationalParkEcoZoneTwin> findById(NationalParkEcoZoneTwin.ZoneId id);
}
