package com.corp.ecosystem.dti.domain.port;

import com.corp.ecosystem.dti.domain.DtiMunicipalityTwin;
import java.util.Optional;

public interface DtiMunicipalityRepositoryPort {
    DtiMunicipalityTwin save(DtiMunicipalityTwin twin);
    Optional<DtiMunicipalityTwin> findById(DtiMunicipalityTwin.MunicipalityId id);
}
