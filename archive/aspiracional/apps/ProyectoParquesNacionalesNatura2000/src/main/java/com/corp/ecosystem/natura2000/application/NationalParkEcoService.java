package com.corp.ecosystem.natura2000.application;

import com.corp.ecosystem.natura2000.domain.NationalParkEcoZoneTwin;
import com.corp.ecosystem.natura2000.domain.port.NationalParkZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class NationalParkEcoService {

    private final NationalParkZoneRepositoryPort repositoryPort;

    public NationalParkEcoService(NationalParkZoneRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public NationalParkEcoZoneTwin monitorEcoZone(
            String tenantId,
            String parkName,
            long h3Index,
            int currentHikers,
            int maxHikers,
            double disturbanceIndex,
            boolean wildfireRisk
    ) {
        NationalParkEcoZoneTwin.ZoneId id = new NationalParkEcoZoneTwin.ZoneId("ZONE-" + System.nanoTime());
        NationalParkEcoZoneTwin.EcoCarryingCapacityMetrics metrics = new NationalParkEcoZoneTwin.EcoCarryingCapacityMetrics(
                currentHikers, maxHikers, disturbanceIndex, wildfireRisk
        );
        NationalParkEcoZoneTwin zone = NationalParkEcoZoneTwin.evaluateZone(id, tenantId, parkName, h3Index, metrics);
        return repositoryPort.save(zone);
    }

    public Optional<NationalParkEcoZoneTwin> getZone(NationalParkEcoZoneTwin.ZoneId id) {
        return repositoryPort.findById(id);
    }
}
