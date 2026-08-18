package com.corp.proyectoeudigitalproductpassport.domain.port.out;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DigitalProductPassportRecordTokenRepositoryPort {
    DigitalProductPassportRecordToken save(DigitalProductPassportRecordToken entity);
    Optional<DigitalProductPassportRecordToken> findById(String id, String tenantId);
}
