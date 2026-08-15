package com.corp.ecosystem.carbonledger.domain.port;

import com.corp.ecosystem.carbonledger.domain.DigitalProductPassport;
import java.util.Optional;

/**
 * Puerto de Persistencia Hexagonal para Pasaportes Digitales.
 */
public interface PassportRepositoryPort {
    DigitalProductPassport save(DigitalProductPassport passport);
    Optional<DigitalProductPassport> findById(DigitalProductPassport.PassportId id);
}
