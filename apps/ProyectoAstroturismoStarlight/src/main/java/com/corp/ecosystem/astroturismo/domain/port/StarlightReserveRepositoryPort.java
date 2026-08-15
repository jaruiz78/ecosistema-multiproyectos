package com.corp.ecosystem.astroturismo.domain.port;

import com.corp.ecosystem.astroturismo.domain.StarlightReserveTwin;
import java.util.Optional;

public interface StarlightReserveRepositoryPort {
    StarlightReserveTwin save(StarlightReserveTwin twin);
    Optional<StarlightReserveTwin> findById(StarlightReserveTwin.ReserveId id);
}
