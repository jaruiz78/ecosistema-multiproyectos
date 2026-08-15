package com.corp.ecosystem.soilbiocarbon.application;

import com.corp.ecosystem.soilbiocarbon.domain.SoilCarbonParcelTwin;
import com.corp.ecosystem.soilbiocarbon.domain.port.SoilParcelRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SoilBioCarbonService {

    private final SoilParcelRepositoryPort repositoryPort;

    public SoilBioCarbonService(SoilParcelRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public SoilCarbonParcelTwin certifyParcelCarbon(
            String tenantId,
            long h3Index,
            double hectares,
            double mycorrhizalRatio,
            double microbialBiomass,
            double respirationFlux,
            double baselineSoc,
            double currentSoc
    ) {
        SoilCarbonParcelTwin.ParcelId id = new SoilCarbonParcelTwin.ParcelId("SOIL-PARCEL-" + System.nanoTime());
        SoilCarbonParcelTwin.SoilMetagenomicProfile metagenomics = new SoilCarbonParcelTwin.SoilMetagenomicProfile(
                mycorrhizalRatio, microbialBiomass, respirationFlux
        );

        SoilCarbonParcelTwin parcel = SoilCarbonParcelTwin.evaluateParcel(
                id, tenantId, h3Index, hectares, metagenomics, baselineSoc, currentSoc
        );
        return repositoryPort.save(parcel);
    }

    public Optional<SoilCarbonParcelTwin> getParcel(SoilCarbonParcelTwin.ParcelId id) {
        return repositoryPort.findById(id);
    }
}
