package com.corp.ecosystem.cruise.domain.port;

import com.corp.ecosystem.cruise.domain.CruiseVoyageMrv;
import java.util.Optional;

public interface CruiseVoyageRepositoryPort {
    CruiseVoyageMrv save(CruiseVoyageMrv voyage);
    Optional<CruiseVoyageMrv> findById(CruiseVoyageMrv.VoyageId id);
}
