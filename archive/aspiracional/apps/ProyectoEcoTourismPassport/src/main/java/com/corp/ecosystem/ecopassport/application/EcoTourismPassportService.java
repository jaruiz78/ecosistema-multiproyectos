package com.corp.ecosystem.ecopassport.application;

import com.corp.ecosystem.ecopassport.domain.EcoTourismPassport;
import com.corp.ecosystem.ecopassport.domain.port.EcoPassportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EcoTourismPassportService {

    private final EcoPassportRepositoryPort repositoryPort;

    public EcoTourismPassportService(EcoPassportRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public EcoTourismPassport issueEcoPassport(
            String tenantId,
            String bookingRef,
            String travelerId,
            String country,
            int travelersCount,
            double transportKgCo2,
            double accommodationKgCo2,
            double activitiesKgCo2,
            double baseTaxRatePerPersonEur,
            String earmarkedProject
    ) {
        EcoTourismPassport.PassportId id = new EcoTourismPassport.PassportId("ECOPASS-" + System.nanoTime());
        EcoTourismPassport.TravelerProfile traveler = new EcoTourismPassport.TravelerProfile(
                travelerId, country, travelersCount
        );

        EcoTourismPassport passport = EcoTourismPassport.calculateAndIssue(
                id,
                tenantId,
                bookingRef,
                traveler,
                transportKgCo2,
                accommodationKgCo2,
                activitiesKgCo2,
                baseTaxRatePerPersonEur,
                earmarkedProject
        );

        return repositoryPort.save(passport);
    }

    public Optional<EcoTourismPassport> getPassport(EcoTourismPassport.PassportId id) {
        return repositoryPort.findById(id);
    }
}
