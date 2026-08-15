package com.corp.ecosystem.ecopassport.domain.port;

import com.corp.ecosystem.ecopassport.domain.EcoTourismPassport;
import java.util.Optional;

public interface EcoPassportRepositoryPort {
    EcoTourismPassport save(EcoTourismPassport passport);
    Optional<EcoTourismPassport> findById(EcoTourismPassport.PassportId id);
}
