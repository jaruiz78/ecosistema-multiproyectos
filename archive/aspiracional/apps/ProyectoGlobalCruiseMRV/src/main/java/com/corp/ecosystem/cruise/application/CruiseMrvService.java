package com.corp.ecosystem.cruise.application;

import com.corp.ecosystem.cruise.domain.CruiseVoyageMrv;
import com.corp.ecosystem.cruise.domain.port.CruiseVoyageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CruiseMrvService {

    private final CruiseVoyageRepositoryPort repositoryPort;

    public CruiseMrvService(CruiseVoyageRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public CruiseVoyageMrv certifyCruiseVoyage(
            String tenantId,
            String imo,
            String lineName,
            double lngTons,
            double greenMethanolTons,
            double mgoTons,
            double ghgIntensity,
            String portUnlocode,
            boolean isColdIroning
    ) {
        CruiseVoyageMrv.VoyageId id = new CruiseVoyageMrv.VoyageId("VOYAGE-" + System.nanoTime());
        CruiseVoyageMrv.FuelMetrics fuel = new CruiseVoyageMrv.FuelMetrics(lngTons, greenMethanolTons, mgoTons, ghgIntensity);
        CruiseVoyageMrv.PortCallEmissionProfile port = new CruiseVoyageMrv.PortCallEmissionProfile(portUnlocode, isColdIroning, isColdIroning ? 0.0 : 4500.0);

        CruiseVoyageMrv voyage = CruiseVoyageMrv.recordVoyage(id, tenantId, imo, lineName, fuel, port);
        return repositoryPort.save(voyage);
    }

    public Optional<CruiseVoyageMrv> getVoyage(CruiseVoyageMrv.VoyageId id) {
        return repositoryPort.findById(id);
    }
}
