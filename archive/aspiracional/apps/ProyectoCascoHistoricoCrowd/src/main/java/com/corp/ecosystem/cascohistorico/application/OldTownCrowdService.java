package com.corp.ecosystem.cascohistorico.application;

import com.corp.ecosystem.cascohistorico.domain.OldTownHeritageZoneTwin;
import com.corp.ecosystem.cascohistorico.domain.port.OldTownZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class OldTownCrowdService {

    private final OldTownZoneRepositoryPort repositoryPort;

    public OldTownCrowdService(OldTownZoneRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public OldTownHeritageZoneTwin monitorQuarter(
            String tenantId,
            String quarterName,
            long h3Index,
            int currentDensity,
            int maxCapacity,
            double noiseDb,
            double qolIndex
    ) {
        OldTownHeritageZoneTwin.ZoneId id = new OldTownHeritageZoneTwin.ZoneId("QUARTER-" + System.nanoTime());
        OldTownHeritageZoneTwin.HeritageFlowMetrics metrics = new OldTownHeritageZoneTwin.HeritageFlowMetrics(
                currentDensity, maxCapacity, noiseDb, qolIndex
        );
        OldTownHeritageZoneTwin zone = OldTownHeritageZoneTwin.evaluateQuarter(id, tenantId, quarterName, h3Index, metrics);
        return repositoryPort.save(zone);
    }

    public Optional<OldTownHeritageZoneTwin> getQuarter(OldTownHeritageZoneTwin.ZoneId id) {
        return repositoryPort.findById(id);
    }
}
