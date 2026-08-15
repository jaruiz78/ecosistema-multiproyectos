package com.corp.ecosystem.soilbiocarbon.domain.port;

import com.corp.ecosystem.soilbiocarbon.domain.SoilCarbonParcelTwin;
import java.util.Optional;

public interface SoilParcelRepositoryPort {
    SoilCarbonParcelTwin save(SoilCarbonParcelTwin parcel);
    Optional<SoilCarbonParcelTwin> findById(SoilCarbonParcelTwin.ParcelId id);
}
