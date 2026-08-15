package com.corp.ecosystem.cascohistorico.domain.port;

import com.corp.ecosystem.cascohistorico.domain.OldTownHeritageZoneTwin;
import java.util.Optional;

public interface OldTownZoneRepositoryPort {
    OldTownHeritageZoneTwin save(OldTownHeritageZoneTwin zone);
    Optional<OldTownHeritageZoneTwin> findById(OldTownHeritageZoneTwin.ZoneId id);
}
