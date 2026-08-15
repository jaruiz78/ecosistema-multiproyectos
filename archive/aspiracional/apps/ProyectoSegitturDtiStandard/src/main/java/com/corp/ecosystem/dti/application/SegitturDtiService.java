package com.corp.ecosystem.dti.application;

import com.corp.ecosystem.dti.domain.DtiMunicipalityTwin;
import com.corp.ecosystem.dti.domain.port.DtiMunicipalityRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SegitturDtiService {

    private final DtiMunicipalityRepositoryPort repositoryPort;

    public SegitturDtiService(DtiMunicipalityRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public DtiMunicipalityTwin auditMunicipality(
            String tenantId,
            String name,
            String community,
            double gov,
            double sust,
            double acc,
            double inn,
            double tech
    ) {
        DtiMunicipalityTwin.MunicipalityId id = new DtiMunicipalityTwin.MunicipalityId("DTI-" + System.nanoTime());
        DtiMunicipalityTwin.DtiAxesScores scores = new DtiMunicipalityTwin.DtiAxesScores(gov, sust, acc, inn, tech);
        DtiMunicipalityTwin twin = DtiMunicipalityTwin.auditDestination(id, tenantId, name, community, scores);
        return repositoryPort.save(twin);
    }

    public Optional<DtiMunicipalityTwin> getMunicipality(DtiMunicipalityTwin.MunicipalityId id) {
        return repositoryPort.findById(id);
    }
}
