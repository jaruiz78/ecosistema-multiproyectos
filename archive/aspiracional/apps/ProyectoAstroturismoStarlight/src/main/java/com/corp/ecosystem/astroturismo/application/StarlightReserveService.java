package com.corp.ecosystem.astroturismo.application;

import com.corp.ecosystem.astroturismo.domain.StarlightReserveTwin;
import com.corp.ecosystem.astroturismo.domain.port.StarlightReserveRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class StarlightReserveService {

    private final StarlightReserveRepositoryPort repositoryPort;

    public StarlightReserveService(StarlightReserveRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public StarlightReserveTwin monitorDarkSky(
            String tenantId,
            String reserveName,
            long h3Index,
            double sqmMagArcsec2,
            double cloudPct,
            double lightPollutionLumens,
            double seeingArcsec
    ) {
        StarlightReserveTwin.ReserveId id = new StarlightReserveTwin.ReserveId("STARLIGHT-" + System.nanoTime());
        StarlightReserveTwin.DarkSkyQualityMetrics metrics = new StarlightReserveTwin.DarkSkyQualityMetrics(
                sqmMagArcsec2, cloudPct, lightPollutionLumens, seeingArcsec
        );
        StarlightReserveTwin twin = StarlightReserveTwin.evaluateSky(id, tenantId, reserveName, h3Index, metrics);
        return repositoryPort.save(twin);
    }

    public Optional<StarlightReserveTwin> getReserve(StarlightReserveTwin.ReserveId id) {
        return repositoryPort.findById(id);
    }
}
