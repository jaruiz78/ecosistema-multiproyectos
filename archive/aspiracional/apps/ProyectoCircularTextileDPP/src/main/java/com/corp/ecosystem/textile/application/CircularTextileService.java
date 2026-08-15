package com.corp.ecosystem.textile.application;

import com.corp.ecosystem.textile.domain.TextileProductPassport;
import com.corp.ecosystem.textile.domain.port.TextilePassportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CircularTextileService {

    private final TextilePassportRepositoryPort repositoryPort;

    public CircularTextileService(TextilePassportRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public TextileProductPassport certifyGarment(
            String tenantId,
            String gtinEan,
            double recycledPolyesterPct,
            double organicCottonPct,
            double elastanePct,
            double recyclabilityScorePct,
            double waterLiters,
            double carbonKg
    ) {
        TextileProductPassport.PassportId id = new TextileProductPassport.PassportId("DPP-TEX-" + System.nanoTime());
        TextileProductPassport.FiberComposition fibers = new TextileProductPassport.FiberComposition(
                recycledPolyesterPct, organicCottonPct, elastanePct, recyclabilityScorePct
        );
        TextileProductPassport.EcoLcaMetrics lca = new TextileProductPassport.EcoLcaMetrics(
                waterLiters, carbonKg, true
        );

        TextileProductPassport passport = TextileProductPassport.issuePassport(id, tenantId, gtinEan, fibers, lca);
        return repositoryPort.save(passport);
    }

    public Optional<TextileProductPassport> getPassport(TextileProductPassport.PassportId id) {
        return repositoryPort.findById(id);
    }
}
